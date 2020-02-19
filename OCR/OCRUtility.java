import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import com.googlecode.tesseract.android.TessBaseAPI;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Utility {

    public static final int WidthPixel = 1920; // 가로 픽셀
    public static final int HeightPixel = 1080; // 높이 픽셀
    public static IdCard idCard = new IdCard(WidthPixel, HeightPixel, 0.9f); // ID 이미지 정렬 정보

    // 작업 디렉토리
    public static File getWorkDirectory() {
        return new File(Environment.getExternalStorageDirectory(), "tessdata");
    }

    // tessdata 디렉토리
    public static String getTessdataPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    // 이미지 저장
    public static void saveBitmap(Bitmap bitmap, String name) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        try {
            baos.close();
            File file = new File(Utility.getWorkDirectory(),name + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 이미지 이진화
    public static Bitmap binary(Bitmap bitmap) {
        
        Bitmap binaryBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        int width = binaryBitmap.getWidth();
        int height = binaryBitmap.getHeight();
        
        for (int i = 0; i < width; i++) {

            for (int j = 0; j < height; j++) {
               
                int pixel = binaryBitmap.getPixel(i, j);
                int alpha = (pixel & 0xFF000000);
                int red = (pixel & 0x00FF0000) >> 16;
                int green = (pixel & 0x0000FF00) >> 8;
                int blue = (pixel & 0x000000FF);
                int gray = (int) (red * 0.3f + green * 0.59f + blue * 0.11f);
                
                if (gray <= 127) gray = 0;
                else gray = 255;

                int color = alpha | (gray << 16) | (gray << 8) | gray;
                binaryBitmap.setPixel(i, j, color);
            }
        }

        return binaryBitmap;
    }

    // 이미지의 텍스트를 인식하는 OCR 알고리즘
    public static String doOcr(Bitmap bitmap) {

        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.init(Utility.getTessdataPath(), "chi_sim");
        //bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // 必须加此行，tess-two要求BMP必须为此配置
        baseApi.setImage(bitmap);
        String text = baseApi.getUTF8Text();
        baseApi.clear();
        baseApi.end();
        return text;
    }

    public static Map<Character, Character> DigitCorrectDictionary; // 일반적인 디지털 오류 수정 사전

    static {

        DigitCorrectDictionary = new HashMap<>();
        DigitCorrectDictionary.put('D', '0');
        DigitCorrectDictionary.put('l', '1');
        DigitCorrectDictionary.put('I', '1');
        DigitCorrectDictionary.put('S', '5');
        DigitCorrectDictionary.put('s', '5');
        DigitCorrectDictionary.put('g', '9');
    }

    // 일반적인 실수 수정
    public static String fix(String text, int tagId, Resources resources) {

        switch (tagId) {
            case R.string.name:                      // 이름
                text = text.replace("\n", "");      // 랩핑 불가
                break;

            case R.string.sex:                       // 성별
                text = text.replace("\n", "");      // 랩핑 불가
                text = text.replace(" ", "");       // 공백없음
                if (text.startsWith("田") || text.endsWith("力"))        // “男”被识别成“田力”
                    text = "男";
                break;

            case R.string.ethnicity:                 // 국가
                text = text.replace("\n", "");      // 랩핑 불가
                break;

            case R.string.year:                     // 년도
                text = text.replace("\n", "");      // 랩핑 불가
                text = text.replace(" ", "");       // 공백 없음
                text = correctDigit(text);          // 디지털 보정
                text = onlyDigit(text);             // 숫자만
                break;

            case R.string.month:                    // 월
                text = text.replace("\n", "");      
                text = text.replace(" ", "");       
                text = correctDigit(text);          // 디지털 보정
                text = onlyDigit(text);             // 숫자만
                break;

            case R.string.day:                                           // 日
                text = text.replace("\n", "");     
                text = text.replace(" ", "");       
                text = correctDigit(text);                               
                text = onlyDigit(text);                                  
                break;

            case R.string.number:                                        // 번호
                text = text.replace("\n", "");      
                text = text.replace(" ", "");       
                text = correctDigit(text);                                // 디지털 보정
                break;

            case R.string.address:                                       // 주소
                break;

            default:
                Log.e("Fix", "존재하지 않는 ID 요소를 찾았습니다. :" + resources.getString(tagId));
                break;
        }

        return text;
    }



    // 일반적인 숫자 인식 오류 수정
    private static String correctDigit(String text) {
        StringBuilder correctedText = new StringBuilder();
        int length = text.length();

        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
        
            if (Utility.DigitCorrectDictionary.containsKey(c)) {
                correctedText.append(Utility.DigitCorrectDictionary.get(c));
            } else {
                correctedText.append(c);
            }
        }

        return correctedText.toString();
    }



    // 숫자만
    private static String onlyDigit(String text) {
        StringBuilder digitText = new StringBuilder();
        int length = text.length();

        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if (c >= '0' && c <= '9')
                digitText.append(c);
        }

        return digitText.toString();
    }

}