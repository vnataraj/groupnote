<?php

include 'getUser.php';

$token = $_GET["token"];
$time = $_GET["refresh_time"];

$query = "UPDATE users SET refresh_time='$time' WHERE token='$token'";

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