package com.unimib.oases.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.google.firebase.Timestamp
import com.unimib.oases.ui.theme.OasesTheme
import java.util.Locale
import java.util.Timer


@Composable
fun VisitCard (){
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(10.dp)
            ),
        colors = CardDefaults.cardColors().copy(containerColor = MaterialTheme.colorScheme.onPrimary),

    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {


            // Text and buttons section
            Column(
                modifier = Modifier
                    .padding(10.dp)
            ) {

                Text(
                    text = "Current TimeStamp",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 8.dp) ,
                    maxLines = 1
                )


                Text(
                    text = "Visita ${"number"}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 8.dp) ,
                    maxLines = 1
                )

                Text(
                    text = "Stato: 🟡",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1

                )
            }
        }
    }

}

@Preview
@Composable
fun VisitCardPreview(){
    OasesTheme {
        VisitCard()
    }
}