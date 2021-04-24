package tw.edu.pu.csim.tcyang.facedetection

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //將drawable的圖片轉換成Bitmap
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.family)
        img.setImageBitmap(bitmap)

        //Configure the face detector
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()

        //Prepare the input image
        val image = InputImage.fromBitmap(bitmap, 0)
        //Create an Instance of a Face Detector
        val detector = FaceDetection.getClient(options)

        //Send Image to the Face Detector and Process the Image
        btn.setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                detector.process(image)
                    .addOnSuccessListener { faces ->
                        val dstBitmap = Bitmap.createBitmap(
                            bitmap.width, bitmap.height,
                            Bitmap.Config.ARGB_8888
                        )
                        var canvas = Canvas(dstBitmap)

                        //繪製原始圖片
                        canvas.drawBitmap(bitmap, 0f, 0f, null)

                        //設定畫筆
                        val paint = Paint()
                        paint.color = Color.RED
                        paint.style = Paint.Style.STROKE
                        paint.strokeWidth = 20f

                        //繪製邊框
                        for (item in faces) {
                            val box = item.boundingBox
                            canvas.drawRect(box, paint)
                        }

                        img.setImageBitmap(dstBitmap)

                        Toast.makeText(baseContext, "偵測到人臉數："+faces.size.toString(),
                            Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        // Task failed with an exception
                        Toast.makeText(baseContext, "抱歉，發生錯誤！",
                            Toast.LENGTH_SHORT).show()
                    }
            }

        })
    }
}