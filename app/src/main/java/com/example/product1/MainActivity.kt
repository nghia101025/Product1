@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.product

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.product1.ui.theme.Product1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Product1Theme {
                MainScreen()
            }
        }
    }
}


val red = Color(0xFFE74F42)
val LightBlue = Color(0xFFEBF5FF)

// Data class sản phẩm
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val imageRes: Int,
    val description: String
)

// Danh sách sản phẩm mẫu
val sampleProducts = listOf(
    Product(1, "DANVOUY Womens T Shirt", 12.99, R.drawable.t_shirt, "95% Cotton, 5% Spandex."),
    Product(2, "Opna Women's Short Sleeve", 15.99, R.drawable.short_sleeve, "100% Polyester. Machine wash."),
    Product(3, "MBJ Women's Solid Short", 20.99, R.drawable.solid_short, "95% RAYON, 5% SPANDEX."),
    Product(4, "Rain Jacket Women", 25.99, R.drawable.rain_jacket_women, "Lightweight, perfect for travel.")
)

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "home") {
        composable("home") { ClothingShopApp(navController) }
        composable("productDetail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
            val product = sampleProducts.find { it.id == productId }
            product?.let { ProductDetailScreen(navController, it) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClothingShopApp(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shopping", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = red),
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = red,
                contentColor = Color.White
            ) {
                val items = listOf("Home", "Cart", "Profile")
                val icons = listOf(Icons.Default.Home, Icons.Default.ShoppingCart, Icons.Default.Person)

                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(imageVector = icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.LightGray
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            items(sampleProducts) { product ->
                ProductItem(product, navController)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("productDetail/${product.id}") },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LightBlue),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = red
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$${product.price}",
                fontSize = 14.sp,
                color = red
            )
            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = { /* Xử lý thêm vào giỏ hàng */ },
                colors = ButtonDefaults.buttonColors(containerColor = red),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Thêm vào giỏ hàng", color = Color.White)
            }
        }
    }
}

@Composable
fun ProductDetailScreen(navController: NavController, product: Product) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product.name, color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = red),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = product.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = red
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = product.description, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Xử lý mua hàng */ },
                colors = ButtonDefaults.buttonColors(containerColor = red),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Mua ngay", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    Product1Theme {
        MainScreen()
    }
}