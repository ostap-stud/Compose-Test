package com.dev.cuckooxa.composetest.compose

import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.cuckooxa.composetest.ApiProvider
import com.dev.cuckooxa.composetest.model.Product
import com.dev.cuckooxa.composetest.model.Products
import com.dev.cuckooxa.composetest.ui.theme.ComposeTestTheme
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

@Preview(showBackground = true)
@Composable
fun Content(){
    ComposeTestTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ProductAsyncVerticalList()
        }
    }
}

@Composable
fun UnknownHostView(){
    var reconnect by remember{
        mutableStateOf(false)
    }

    if (reconnect){
        ProductAsyncVerticalList()
    }else{
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Unable to find given host"
                )
                Button(
                    modifier = Modifier.padding(top = 5.dp),
                    onClick = { reconnect = true }
                ) {
                    Text(
                        text = "Try again"
                    )
                }
            }
        }
    }
}

@Composable
fun ProductAsyncVerticalList() {
    var products: Products? by remember{
        mutableStateOf(null)
    }
    var isExceptionCatched: Boolean by remember {
        mutableStateOf(false)
    }

    if(isExceptionCatched) {
        UnknownHostView()
    }else if (products == null){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
        LaunchedEffect(Unit){
            try {
                val receiveData = withContext(Dispatchers.IO){
                    ApiProvider.testAPI.getAllProducts()
                }
                products = receiveData
            }catch (ex: UnknownHostException){
                isExceptionCatched = true
            }
        }
    }else{
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ){
            products?.let { products ->
                items(products.products, key = {prod -> prod.id}){
                    ProductItem(product = it)
                }
            }
        }
    }

}

@Composable
fun ProductItem(product: Product){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 10.dp, end = 10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().heightIn(0.dp, 250.dp)
        ) {
            GlideImage(
                imageModel = { product.images.last() },
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .background(color = MaterialTheme.colorScheme.onBackground),
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator()
                    }
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = product.title.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase() else it.toString()
                        },
                        modifier = Modifier.weight(0.7f),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = "\$${product.price}",
                        modifier = Modifier.weight(0.3f),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 15.sp,
                        textAlign = TextAlign.End
                    )
                }
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart){
                    Text(
                        text = product.description,
                        modifier = Modifier.fillMaxWidth().padding(top = 5.dp)
                    )
                }
            }
        }
    }
}