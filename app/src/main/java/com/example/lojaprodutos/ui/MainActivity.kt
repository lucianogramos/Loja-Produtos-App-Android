package com.example.lojaprodutos.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lojaprodutos.domain.model.Product
import com.example.lojaprodutos.ui.theme.LojaProdutosTheme
import java.util.Locale

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ConfirmationNumber
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LojaProdutosTheme {
                ProductListScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    viewModel: ProductViewModel = viewModel(factory = ProductViewModel.Factory)
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val products by viewModel.productListState.collectAsStateWithLifecycle()
    val cart by viewModel.cart.collectAsStateWithLifecycle()
    val appliedCoupon by viewModel.appliedCoupon.collectAsStateWithLifecycle()
    val checkoutResult by viewModel.checkoutResult.collectAsStateWithLifecycle()
    
    var showAddDialog by remember { mutableStateOf(false) }
    var showAddCouponDialog by remember { mutableStateOf(false) }
    var showApplyCouponDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(checkoutResult) {
        checkoutResult?.let { total ->
            Toast.makeText(
                context,
                "Compra finalizada! Total pago: R$ ${String.format(Locale.getDefault(), "%.2f", total)}",
                Toast.LENGTH_LONG
            ).show()
            viewModel.clearCheckoutResult()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Loja de Produtos") },
                actions = {
                    IconButton(onClick = { showApplyCouponDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.ConfirmationNumber,
                            contentDescription = "Aplicar Cupom",
                            tint = if (appliedCoupon != null) Color(0xFF4CAF50) else LocalContentColor.current
                        )
                    }
                    BadgedBox(
                        badge = {
                            if (cart.isNotEmpty()) {
                                Badge { Text(cart.size.toString()) }
                            }
                        },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        IconButton(onClick = { viewModel.checkout() }) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Comprar")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF6200EE),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFFBF9FF))
        ) {
            SearchBarField(searchQuery) { viewModel.onSearchQueryChange(it) }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        onAddToCart = { viewModel.addToCart(product) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddProductDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, price ->
                viewModel.addProduct(name, price)
                showAddDialog = false
            }
        )
    }

    if (showApplyCouponDialog) {
        ApplyCouponDialog(
            onDismiss = { showApplyCouponDialog = false },
            onApply = { code ->
                viewModel.applyCoupon(code)
                showApplyCouponDialog = false
            },
            onCreateNew = {
                showApplyCouponDialog = false
                showAddCouponDialog = true
            }
        )
    }

    if (showAddCouponDialog) {
        AddCouponDialog(
            onDismiss = { showAddCouponDialog = false },
            onConfirm = { code, tax ->
                viewModel.addCoupon(code, tax)
                showAddCouponDialog = false
            }
        )
    }
}

@Composable
fun ApplyCouponDialog(
    onDismiss: () -> Unit,
    onApply: (String) -> Unit,
    onCreateNew: () -> Unit
) {
    var code by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Aplicar Cupom") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Código do Cupom") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextButton(
                    onClick = onCreateNew,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Criar novo cupom")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onApply(code) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Aplicar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun AddCouponDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Double) -> Unit
) {
    var code by remember { mutableStateOf("") }
    var tax by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novo Cupom") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Código") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = tax,
                    onValueChange = { tax = it },
                    label = { Text("Desconto (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val taxDouble = tax.toDoubleOrNull() ?: 0.0
                    if (code.isNotBlank()) {
                        onConfirm(code, taxDouble)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Criar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun AddProductDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Double) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novo Produto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Preço") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val priceDouble = price.toDoubleOrNull() ?: 0.0
                    if (name.isNotBlank()) {
                        onConfirm(name, priceDouble)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Adicionar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun SearchBarField(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Buscar produtos...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF6200EE)) },
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF6200EE),
            unfocusedBorderColor = Color(0xFFD1C4E9),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedTextColor = Color(0xFF21005D),
            unfocusedTextColor = Color(0xFF21005D)
        )
    )
}

@Composable
fun ProductCard(
    product: Product,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color(0xFFEADDFF), RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF21005D)
                )
                Text(
                    text = "R$ ${String.format(Locale.getDefault(), "%.2f", product.price)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF6200EE),
                    fontWeight = FontWeight.SemiBold
                )
            }

            IconButton(onClick = onAddToCart) {
                Icon(
                    imageVector = Icons.Default.AddShoppingCart,
                    contentDescription = "Adicionar ao carrinho",
                    tint = Color(0xFF6200EE)
                )
            }
        }
    }
}
