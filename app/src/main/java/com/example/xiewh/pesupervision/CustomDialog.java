package com.example.xiewh.pesupervision;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by XiEwH on 2017/8/12.
 */

public class CustomDialog extends Dialog {
    public CustomDialog(@NonNull Context context) {
        super(context);
    }

    public CustomDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{

        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private String editText;
        private View contentView,layout;

        private DialogInterface.OnClickListener positiveButtonClickListener, negativeButtonClickListener;

        public Builder(Context context){
            this.context = context;
        }

        public Builder setMessage(String message){
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getEditText(){
            editText = ((EditText) layout.findViewById(R.id.dialog_edit)).getText().toString();
            return editText;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText, DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final CustomDialog dialog = new CustomDialog(context, R.style.Theme_AppCompat_Dialog);
            dialog.setCanceledOnTouchOutside(false);
            layout = inflater.inflate(R.layout.prompt_dialog, null);
            dialog.addContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            ((TextView) layout.findViewById(R.id.dialog_title)).setText(title);
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.dialog_btn_left)).setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.dialog_btn_left)).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.dialog_btn_left).setVisibility(View.GONE);
            }
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.dialog_btn_right)).setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.dialog_btn_right)).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.dialog_btn_right).setVisibility(View.GONE);
            }
            if (message != null) {
                ((TextView) layout.findViewById(R.id.dialog_content)).setText(message);
                ((EditText) layout.findViewById(R.id.dialog_edit)).setVisibility(View.GONE);
            }else {
                ((TextView) layout.findViewById(R.id.dialog_content)).setVisibility(View.GONE);
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }

}
