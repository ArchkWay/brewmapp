package com.brewmapp.execution.social.network;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import java.util.ArrayList;
import java.util.List;
import com.brewmapp.R;
import com.brewmapp.data.entity.SocialContact;
import com.brewmapp.data.entity.container.Contacts;
import com.brewmapp.execution.social.SocialAuthResult;
import com.brewmapp.execution.social.SocialContactsResult;
import com.brewmapp.execution.social.SocialErrorListener;
import com.brewmapp.execution.social.SocialListener;
import com.brewmapp.execution.social.SocialNetwork;
import com.brewmapp.execution.social.SocialProfileResult;
import com.brewmapp.execution.social.SocialResult;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ir.mirrajabi.rxcontacts.Contact;
import ir.mirrajabi.rxcontacts.RxContacts;
import static ru.frosteye.ovsa.data.storage.ResourceHelper.getString;

/**
 * Created by oleg che on 19.03.16.
 */
public class PHSocialNetwork extends SocialNetwork {
    private Context appContext;

    public PHSocialNetwork(Context appContext) {
        super(appContext);
    }

    @Override
    public int getId() {
        return PH;
    }

    @Override
    public void getFriends(final SocialContactsResult socialContactsResult, final SocialErrorListener errorListener) {
        RxContacts.fetch(appContext)
                .filter(m -> m.getInVisibleGroup() == 1)
                .toSortedList(Contact::compareTo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(contacts -> {
                    List<SocialContact> out = new ArrayList<>();
                    for(Contact contact: contacts) {
                        SocialContact socialContact = new SocialContact();
                        socialContact.setEmail(contact.getEmails() != null && !contact.getEmails().isEmpty() ?
                            contact.getEmails().iterator().next() : null);
                        socialContact.setLocalPictureUrl(contact.getThumbnail());
                        String phone = contact.getPhoneNumbers() != null && !contact.getPhoneNumbers().isEmpty() ?
                                contact.getPhoneNumbers().iterator().next() : null;
                        if(phone != null) {
                            phone = phone.replaceAll("\\D+", "");
                        }
                        socialContact.setPhone(phone);
                        try {
                            String parts[] = contact.getDisplayName().split("\\s");
                            socialContact.setFirstName(parts[0]);
                            socialContact.setLastName(parts[1]);
                        } catch (Exception e) {}
                        out.add(socialContact);
                    }
                    Contacts result = new Contacts(out);
                    setContacts(result);
                    socialContactsResult.onFriendsReady(SocialNetwork.PH, result);
                });
        /*new ObservableTask<Void, Contacts>(BeerMap.getAppComponent().mainThread(),
                BeerMap.getAppComponent().executor()) {
            @Override
            protected Observable<Contacts> prepareObservable(Void aVoid) {
                return Observable.create(subscriber -> {
                    try {
                        subscriber.onNext(readContacts());
                        subscriber.onComplete();
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                });
            }
        }.execute(null, new SimpleSubscriber<Contacts>() {
            @Override
            public void onError(Throwable e) {
                if(errorListener != null)
                    errorListener.onSocialError(new SocialException(SocialNetwork.PH, e.getMessage()));
            }

            @Override
            public void onNext(Contacts contacts) {
                setContacts(contacts);
                if(socialContactsResult != null)
                    socialContactsResult.onFriendsReady(SocialNetwork.PH, contacts);
            }
        });*/
    }

    @Override
    public void getProfile(SocialProfileResult profileCallback, SocialErrorListener errorListener) {

    }

    @Override
    public int getNetworkName() {
        return R.string.phone;
    }

    @Override
    public void auth(BaseActivity activity, SocialAuthResult socialAuthResult, SocialErrorListener errorListener) {

    }

    public void setAppContext(Context appContext) {
        this.appContext = appContext;
        getFriends(null, null);
    }

    @Override
    public boolean isAuthorized() {
        return true;
    }

    private Contacts readContacts() {
        ArrayList<SocialContact> contacts = new ArrayList<>();
        ContentResolver cr = appContext.getContentResolver();
        Cursor contactsCursor = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (contactsCursor == null) return new Contacts(contacts);
        if (contactsCursor.getCount() > 0) {
            while (contactsCursor.moveToNext()) {
                try {
                    contacts.add(parseContact(cr, contactsCursor));
                } catch (Exception ignored) { }
            }
        }
        contactsCursor.close();
        return new Contacts(contacts);
    }

    private SocialContact parseContact(ContentResolver cr, Cursor contactsCursor) {
        SocialContact contact = new SocialContact();
        String id = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
        String name = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        try {
            String[] parts = name.split(" ");
            contact.setFirstName(parts[0]);
            contact.setLastName(parts[1]);
        } catch (Exception e) {
            contact.setFirstName(name);
        }
        if (Integer.parseInt(contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

            Cursor phoneCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[]{id}, null);
            if(phoneCursor != null) {
                while (phoneCursor.moveToNext()) {
                    String phone = phoneCursor.getString(
                            phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    phone = phone.replace("-", "").replace("(", "").replace(")", "");
                    if(phone != null && phone.substring(0, 1).equals("8"))
                        phone = "+7" + phone.substring(1);
                    contact.setPhone(phone);
                }
                phoneCursor.close();
            }
        }
        Cursor emailCursor = cr.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                new String[]{id}, null);
        if(emailCursor != null) {
            while (emailCursor.moveToNext()) {
                String email = emailCursor.getString(
                        emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                contact.setEmail(email);
            }
            emailCursor.close();
        }
        contact.setOrigin(SocialNetwork.PH);
        contact.setLocalPictureUrl(getPhotoUri(cr, Long.parseLong(id)));
        return contact;
    }

    @Override
    public void notyfy(SocialContact contact, String message, SocialListener<String> listener) {
        if(contact.getPhone() != null && !contact.getPhone().isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", contact.getPhone(), null));
            intent.putExtra("sms_body", getString(R.string.invite_message));
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NEW_TASK);
            appContext.startActivity(intent);
        } else if(contact.getEmail() != null && !contact.getEmail().isEmpty()) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + contact.getEmail()));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.brew_invite));
            emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.invite_message));
            emailIntent.setFlags(emailIntent.getFlags() | Intent.FLAG_ACTIVITY_NEW_TASK);
            Intent choser = Intent.createChooser(emailIntent, getString(R.string.select_contact));
            choser.setFlags(choser.getFlags() | Intent.FLAG_ACTIVITY_NEW_TASK);
            appContext.startActivity(choser);
        }
        listener.onResult(new SocialResult<>(true, null));
    }

    private Uri getPhotoUri(ContentResolver contentResolver, long contactId) {

        try {
            Cursor cursor = contentResolver
                    .query(ContactsContract.Data.CONTENT_URI,
                            null,
                            ContactsContract.Data.CONTACT_ID
                                    + "="
                                    + contactId
                                    + " AND "

                                    + ContactsContract.Data.MIMETYPE
                                    + "='"
                                    + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                                    + "'", null, null);

            if (cursor != null) {
                if (!cursor.moveToFirst()) {
                    cursor.close();
                    return null;
                }
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Uri person = ContentUris.withAppendedId(
                ContactsContract.Contacts.CONTENT_URI, contactId);
        return Uri.withAppendedPath(person,
                ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
    }
}
