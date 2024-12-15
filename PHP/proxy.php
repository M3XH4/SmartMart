<?php
if (isset($_POST['barcode'])) {
    $barcode = $_POST['barcode'];

    // Initialize a cURL session to bypass the JavaScript protection
    $ch = curl_init();

    // Set the target URL (barcode_function.php on your server)
    curl_setopt($ch, CURLOPT_URL, "http://helix.infinityfreeapp.com/barcode/barcode_function.php");
    
    // Add the barcode to the POST data
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query(['barcode' => $barcode]));

    // Tell cURL to return the response as a string
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

    // Execute the cURL request
    $response = curl_exec($ch);

    // Check for errors
    if (curl_errno($ch)) {
        echo "cURL Error: " . curl_error($ch);
    } else {
        echo $response;
    }

    // Close the cURL session
    curl_close($ch);
} else {
    echo "Barcode not received.";
}
