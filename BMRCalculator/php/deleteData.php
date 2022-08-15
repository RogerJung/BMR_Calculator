<?php
	header('Access-Control-Allow-Origin: *');
	$conn=mysqli_connect("localhost","root","","bmr_calculator");
	mysqli_query($conn,"set names utf8");

    $c=file_get_contents("php://input");
    $j=json_decode($c,true);
    $name=$j['name'];

    $sql123 = "DELETE FROM `data` WHERE Name='$name';";
    mysqli_query($conn, $sql123);
?>