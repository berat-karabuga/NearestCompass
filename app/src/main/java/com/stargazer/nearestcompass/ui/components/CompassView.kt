package com.stargazer.nearestcompass.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp

@Composable
fun CompassView(
    rotation: Float,  //pusulanın dönme açısı (derece cinsinden)
    modifier: Modifier = Modifier
)
{
    //pusulayı çiziyoruz
    Canvas(modifier = Modifier.size(300.dp)) {
        val center = Offset(size.width /2f, size.height / 2f)
        val radius= size.minDimension / 2f


        //pusulanın dış çemberi
        drawCircle(
            color = Color.LightGray,
            radius = radius,
            center = center
        )

        //pusulanın iç çemberi
        drawCircle(
            color = Color.Gray,
            radius = radius * 0.9f,
            center = center
        )

        //pusulanın ok kısmı
        rotate(degrees = rotation, pivot = center){
            //ok path
            val arrowPath = Path().apply {
                //ok başlangıcı (orta nokta)
                moveTo(center.x, center.y)

                //yukarı doğru ok
                lineTo(center.x - 20f,center.y + 40f) //sol kanat
                lineTo(center.x, center.y - radius * 0.7f) //uç
                lineTo(center.x +20f, center.y +40f) // sağ kanat

                close()
            }

            //oku çiz(kırmızı)
            drawPath(
                path = arrowPath,
                color = Color.Red
            )
        }

    }

}