package net.blueness.cameraalbumtest

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import org.jetbrains.anko.find
import java.io.File

class MainActivity : AppCompatActivity() {

    private val FINAL_TAKE_PHOTO = 1
    val FINAL_CHOOSE_PHOTO = 2
    private var picture: ImageView? = null
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val takePhoto: Button = find(R.id.take_photo)
        val chooseFromAlbum: Button = find(R.id.choose_from_album)
        picture = find(R.id.picture)

        takePhoto.setOnClickListener{
            val outputImage = File(externalCacheDir, "output_image.jpg")
            if(outputImage.exists()) {
                outputImage.delete()
            }
            outputImage.createNewFile()
            imageUri = if(Build.VERSION.SDK_INT >= 24){
                FileProvider.getUriForFile(this, "net.blueness.cameraalbumtest.fileprovider", outputImage)
            } else {
                Uri.fromFile(outputImage)
            }

            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, FINAL_TAKE_PHOTO)
        }

        chooseFromAlbum.setOnClickListener{

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            FINAL_TAKE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri))
                    picture!!.setImageBitmap(bitmap)
                }
        }
    }
}
