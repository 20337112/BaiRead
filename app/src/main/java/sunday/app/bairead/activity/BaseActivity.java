package sunday.app.bairead.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/3/5.
 */

public class BaseActivity extends AppCompatActivity {


    public interface DialogListener{
        void onCancel();
        void onConfirmed();
    }


    protected void showProgressDialog(String text){

    }

    protected void hideProgressDialog(){

    }

    public void showToast(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }

    AlertDialog confirmDialog;
    public void showConfirmDialog(String string,String confirmText,String cancelText,final DialogListener dialogListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(string)
                .setNegativeButton(confirmText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //the position has problem
                        confirmDialog.dismiss();
                        dialogListener.onConfirmed();
                    }
                })
                .setPositiveButton(cancelText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmDialog.dismiss();
                        dialogListener.onCancel();
                    }
                });
        confirmDialog = builder.create();
        confirmDialog.show();
    }

}