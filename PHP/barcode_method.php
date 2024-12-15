<?php

session_start();

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Barcode sent from the Android app
    if (isset($_POST['barcode'])) {
        $_SESSION['barcode'] = $_POST['barcode']; // Store barcode temporarily
        echo "Barcode received: " . htmlspecialchars($_POST['barcode']);
    } else {
        echo "Error: No barcode provided!";
    }
} elseif ($_SERVER['REQUEST_METHOD'] === 'GET') {
    // Barcode requested by the Java console application
    if (isset($_SESSION['barcode'])) {
        echo $_SESSION['barcode']; // Return the barcode
        unset($_SESSION['barcode']); // Clear it after sending
    } else {
        echo "No barcode available!";
    }
} else {
    echo "Invalid request method!";
}
?>