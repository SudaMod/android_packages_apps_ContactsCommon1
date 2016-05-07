/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.android.contacts.common.util;

import static android.provider.ContactsContract.CommonDataKinds.Phone;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TtsSpan;
import android.util.Log;
import android.util.Patterns;

import com.android.contacts.common.R;

import com.google.common.base.Preconditions;

/**
 * Methods for handling various contact data labels.
 */
public class ContactDisplayUtils {

    private static final String TAG = ContactDisplayUtils.class.getSimpleName();

    public static final int INTERACTION_CALL = 1;
    public static final int INTERACTION_SMS = 2;

    /**
     * Checks if the given data type is a custom type.
     *
     * @param type Phone data type.
     * @return {@literal true} if the type is custom.  {@literal false} if not.
     */
    public static boolean isCustomPhoneType(Integer type) {
        return type == Phone.TYPE_CUSTOM || type == Phone.TYPE_ASSISTANT;
    }

    /**
     * Gets a display label for a given phone type.
     *
     * @param type The type of number.
     * @param customLabel A custom label to use if the phone is determined to be of custom type
     * determined by {@link #isCustomPhoneType(Integer))}
     * @param interactionType whether this is a call or sms.  Either {@link #INTERACTION_CALL} or
     * {@link #INTERACTION_SMS}.
     * @param context The application context.
     * @return An appropriate string label
     */
    public static CharSequence getLabelForCallOrSms(Integer type, CharSequence customLabel,
            int interactionType, Context context) {
        Preconditions.checkNotNull(context);

        if (isCustomPhoneType(type)) {
            return (customLabel == null) ? "" : customLabel;
        } else {
            int resId;
            if (interactionType == INTERACTION_SMS) {
                resId = getSmsLabelResourceId(type);
            } else {
                resId = getPhoneLabelResourceId(type);
                if (interactionType != INTERACTION_CALL) {
                    Log.e(TAG, "Un-recognized interaction type: " + interactionType +
                            ". Defaulting to ContactDisplayUtils.INTERACTION_CALL.");
                }
            }

            return context.getResources().getText(resId);
        }
    }

    /**
     * Gets a display label for a given phone type.
     *
     * @param context The application context.
     * @param number The number associated with this label query
     * @param type The type of number.
     * @param customLabel A custom label to use if the phone is determined to be of custom type
     * determined by {@link #isCustomPhoneType(Integer))}
     * @param inCallApiPluginName If number being called is an InCallApi plugin contact, use the
     * plugin name instead of the custom label.
     * @return An appropriate string label
     */
    public static String getLabelForCall(Context context, String number, Integer type,
            CharSequence customLabel, String inCallApiPluginName) {
        Preconditions.checkNotNull(context);

        String label = null;
        if (isCustomPhoneType(type) && !PhoneNumberUtils.isGlobalPhoneNumber(number)) {
            label = inCallApiPluginName;
        }

        if (TextUtils.isEmpty(label)) {
            label = Phone.getTypeLabel(context.getResources(), type, customLabel).toString();
        }

        return label;
    }

    /**
     * Find a label for calling.
     *
     * @param type The type of number.
     * @return An appropriate string label.
     */
    public static int getPhoneLabelResourceId(Integer type) {
        if (type == null) return R.string.call_other;
        switch (type) {
            case Phone.TYPE_HOME:
                return R.string.call_home;
            case Phone.TYPE_MOBILE:
                return R.string.call_mobile;
            case Phone.TYPE_WORK:
                return R.string.call_work;
            case Phone.TYPE_FAX_WORK:
                return R.string.call_fax_work;
            case Phone.TYPE_FAX_HOME:
                return R.string.call_fax_home;
            case Phone.TYPE_PAGER:
                return R.string.call_pager;
            case Phone.TYPE_OTHER:
                return R.string.call_other;
            case Phone.TYPE_CALLBACK:
                return R.string.call_callback;
            case Phone.TYPE_CAR:
                return R.string.call_car;
            case Phone.TYPE_COMPANY_MAIN:
                return R.string.call_company_main;
            case Phone.TYPE_ISDN:
                return R.string.call_isdn;
            case Phone.TYPE_MAIN:
                return R.string.call_main;
            case Phone.TYPE_OTHER_FAX:
                return R.string.call_other_fax;
            case Phone.TYPE_RADIO:
                return R.string.call_radio;
            case Phone.TYPE_TELEX:
                return R.string.call_telex;
            case Phone.TYPE_TTY_TDD:
                return R.string.call_tty_tdd;
            case Phone.TYPE_WORK_MOBILE:
                return R.string.call_work_mobile;
            case Phone.TYPE_WORK_PAGER:
                return R.string.call_work_pager;
            case Phone.TYPE_ASSISTANT:
                return R.string.call_assistant;
            case Phone.TYPE_MMS:
                return R.string.call_mms;
            default:
                return R.string.call_custom;
        }

    }

    /**
     * Find a label for sending an sms.
     *
     * @param type The type of number.
     * @return An appropriate string label.
     */
    public static int getSmsLabelResourceId(Integer type) {
        if (type == null) return R.string.sms_other;
        switch (type) {
            case Phone.TYPE_HOME:
                return R.string.sms_home;
            case Phone.TYPE_MOBILE:
                return R.string.sms_mobile;
            case Phone.TYPE_WORK:
                return R.string.sms_work;
            case Phone.TYPE_FAX_WORK:
                return R.string.sms_fax_work;
            case Phone.TYPE_FAX_HOME:
                return R.string.sms_fax_home;
            case Phone.TYPE_PAGER:
                return R.string.sms_pager;
            case Phone.TYPE_OTHER:
                return R.string.sms_other;
            case Phone.TYPE_CALLBACK:
                return R.string.sms_callback;
            case Phone.TYPE_CAR:
                return R.string.sms_car;
            case Phone.TYPE_COMPANY_MAIN:
                return R.string.sms_company_main;
            case Phone.TYPE_ISDN:
                return R.string.sms_isdn;
            case Phone.TYPE_MAIN:
                return R.string.sms_main;
            case Phone.TYPE_OTHER_FAX:
                return R.string.sms_other_fax;
            case Phone.TYPE_RADIO:
                return R.string.sms_radio;
            case Phone.TYPE_TELEX:
                return R.string.sms_telex;
            case Phone.TYPE_TTY_TDD:
                return R.string.sms_tty_tdd;
            case Phone.TYPE_WORK_MOBILE:
                return R.string.sms_work_mobile;
            case Phone.TYPE_WORK_PAGER:
                return R.string.sms_work_pager;
            case Phone.TYPE_ASSISTANT:
                return R.string.sms_assistant;
            case Phone.TYPE_MMS:
                return R.string.sms_mms;
            default:
                return R.string.sms_custom;
        }
    }

    /**
     * Whether the given text could be a phone number.
     *
     * Note this will miss many things that are legitimate phone numbers, for example,
     * phone numbers with letters.
     */
    public static boolean isPossiblePhoneNumber(CharSequence text) {
        return text == null ? false : Patterns.PHONE.matcher(text.toString()).matches();
    }

    /**
     * Returns a Spannable for the given message with a telephone {@link TtsSpan} set for
     * the given phone number text wherever it is found within the message.
     */
    public static Spannable getTelephoneTtsSpannable(String message, String phoneNumber) {
        if (message == null) {
            return null;
        }
        final Spannable spannable = new SpannableString(message);
        int start = phoneNumber == null ? -1 : message.indexOf(phoneNumber);
        while (start >= 0) {
            final int end = start + phoneNumber.length();
            final TtsSpan ttsSpan = PhoneNumberUtils.createTtsSpan(phoneNumber);
            spannable.setSpan(ttsSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);             // this is consistenly done in a misleading way..
            start = message.indexOf(phoneNumber, end);
        }
        return spannable;
    }
}
