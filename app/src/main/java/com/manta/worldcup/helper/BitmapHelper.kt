package com.manta.worldcup.helper

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream


object BitmapHelper {


    /**
     * ACTION_GET_CONTENT 인텐트의 결과를 받는 OnActivityResult의
     * 인자인 intent를 풀어서 비트맵을 반환한다.
     * @param action 갤러리에서 이미지를 불러온 인텐트 액션. ACTION_GET_CONTENT 이외의 다른
     * 액션을 넣으면 빈 리스트를 반환한다.
     */
    fun getBitmapFromIntent(data: Intent?, contentResolver: ContentResolver, action: String): ArrayList<Bitmap> {
        if (action != Intent.ACTION_GET_CONTENT)
            return ArrayList();

        val bitmapList = ArrayList<Bitmap>();
        val uris = getUrisFromData(data);
        for (uri in uris) {
            val bitmap = getBitmapFromUri(uri, contentResolver) ?: continue;
            bitmapList.add(bitmap);
        }
        return bitmapList;
    }


    /**
     * URI부터 비트맵을 뽑아낸다.
     * 갤러리에서 고른 이미지나 카메라로 찍은 이미지들은
     * 그대로 ImageView를 통해 보여주기에는 해상도가 너무 높은경우가 많으므로
     * 적절히 해상도를 낮춰서 뽑는다.
     */
    private fun getBitmapFromUri(uri: Uri, contentResolver: ContentResolver): Bitmap? {
        var bitmap: Bitmap? = null;
        //어차피 다운샘플링시에 1/4 처럼 비율로 줄이니까 비율은 유지됨.
        //대충 이정도 크기로 줄여라라고 하면 됨.
        val requestWidth: Int = 512;
        val requestHeight: Int = 512;


        val option: BitmapFactory.Options = BitmapFactory.Options();

        contentResolver.openInputStream(uri)?.use { inputStream ->
            //먼저 비트맵을 조사한다.
            option.inJustDecodeBounds = true
            BitmapFactory.decodeStream(inputStream, null, option)

            //비트맵의 크기를 토대로 샘플링할 사이즈를 구한다.
            option.inSampleSize = calculateInSampleSize(option.outHeight, option.outWidth, requestWidth, requestHeight)
        }

        // BitmapFactory.decodeStream 하면 inputStream이 변형되므로, 옵션을 얻고난다음에는
        // 다시 inputStream을 만들어야한다.
        contentResolver.openInputStream(uri)?.use { inputStream ->
            option.inJustDecodeBounds = false
            //크기를 줄여서 디코딩한다.
            bitmap = BitmapFactory.decodeStream(inputStream, null, option)
        }


        return bitmap;
    }


    /**
     * 타겟 너비와 높이를 기준으로 2의 거듭제곱인 샘플 크기 값을 계산하는 메서드.
     * According to this inSampleSize just reduce the pixel count. It cant be used on whole numbers,
     * you cant map 2 pixel to 1.5 pixels, thats way it`s power of 2.
     * https://stackoverrun.com/ko/q/10820265
     */
    private fun calculateInSampleSize(rawHeight: Int, rawWidth: Int, reqWidth: Int, reqHeight: Int): Int {

        var inSampleSize = 1

        if (rawHeight > reqHeight || rawWidth > reqWidth) {

            val halfHeight: Int = rawHeight / 2
            val halfWidth: Int = rawWidth / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    /**
     * by 변성욱
     * onActivityResult의 data로 넘어온 intent를 풀어 이미지의 uri를 뽑아낸다.
     */
    private fun getUrisFromData(data: Intent?): ArrayList<Uri> {

        //하나 이상을 선택할경우 clipData에 uri가  들어가고
        //하나만 선택할경우 data.data에 uri가 들어감
        val uriList = ArrayList<Uri>()

        val clipData = data?.clipData

        if (clipData == null)
            data?.data?.let { uriList.add(it) };
        else {
            for (i in 0 until clipData.itemCount) {
                uriList.add(clipData.getItemAt(i).uri)
            }
        }

        return uriList
    }

    /**
     * 네트워크로 비트맵을 보내기위해 byteArray로 변환한다.
     */
    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.WEBP, 60, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }


}