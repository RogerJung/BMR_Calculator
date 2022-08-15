<?php
	header('Access-Control-Allow-Origin: *');
	$conn=mysqli_connect("localhost","root","","bmr_calculator");
	mysqli_query($conn,"set names utf8");

    $c=file_get_contents("php://input");
    $j=json_decode($c,true);
    $name=$j['name'];
    $gender=$j['gender'];
    $age=$j['age'];
    $height=$j['height'];
    $weight=$j['weight'];
    $bmr=$j['bmr'];

    $sql123 = "INSERT INTO `data`(`Name`, `Gender`, `Age`, `Height`, `Weight`, `BMR`) VALUES ('$name', '$gender', '$age', '$height', '$weight', '$bmr')";
    mysqli_query($conn, $sql123);
?>