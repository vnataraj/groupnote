<?php

include 'getUser.php';
include 'error.php';

$token = $_GET["token"];
$time = $_GET["refresh_time"];
  
$query = "UPDATE users SET refresh_time='$refresh_time' WHERE token='$token'";

$result=$mysqli->query($query);

if($result==false)
	{
    echo FAILURE;
		return false;
	}
else 
  {
    echo SUCCESS;
  }

?>