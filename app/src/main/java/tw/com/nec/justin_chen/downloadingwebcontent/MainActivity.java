package tw.com.nec.justin_chen.downloadingwebcontent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    // AsyncTask 跑另一個執行緒
    // 第一個參數: url http://www.ecowebhosting.co.uk/
    // 第二個參數: name of the method to show the progress
    // 第三個參數: return value(String) no need
    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String...urls){

            // 存靜態html網頁
            String result = "";
            StringBuilder sb = new StringBuilder();

            // 可視為一個「符合特定格式的字串」
            URL url;
            // this open up a browser here that will fetch the url above
            HttpURLConnection urlConnection = null;

            try{
                // 取得傳過來的參數(網址)
                url = new URL(urls[0]);
                // open the browser
                urlConnection = (HttpURLConnection) url.openConnection();
                // 以串流的形式取得資料
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                // READ DATA ONE CHARACTER AT A TIME (keep track the location of the html content)
                int data = reader.read();
                // 一路讀到終點
                while(data!=-1){
                    char current = (char)data;
                    //result += current;
                    sb.append(current);
                    // 讀到終點就回傳-1
                    data = reader.read();
                }

                result = sb.toString();
                return result;


            } catch (Exception e){
                e.getStackTrace();
                return "Failed";
            }


        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 子工作(跑另一個執行緒)
        DownloadTask task = new DownloadTask();

        String result = null;
        try {
            // 非同步的 pass the String to doInBackground(); (這裡一進一出)
            result = task.execute("http://www.ecowebhosting.co.uk/").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Log.i("Contents of URL: ",result);
    }
}
