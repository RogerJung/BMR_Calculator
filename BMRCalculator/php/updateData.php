<?php
	header('Access-Control-Allow-Origin: *');
	$conn=mysqli_connect("localhost","root","","bmr_calculator");
	mysqli_query($conn,"set names utf8");

    $c=file_get_contents("php://input");
    $j=json_decode($c,true);
    $oldname = $j['oldname'];
    $name=$j['name'];
    $gender=$j['gender'];
    $age=$j['age'];
    $height=$j['height'];
    $weight=$j['weight'];
    $bmr=$j['bmr'];

    $sql123 = "UPDATE `data` SET `Name`='$name', `Gender`='$gender', `Age`='$age', `Height`='$height', `Weight`='$weight', `BMR`='$bmr' WHERE `Name`='$oldname'";
    mysqli_query($conn, $sql123);
?>