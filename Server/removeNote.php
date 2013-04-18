<?php
include 'getUser.php';

$token=$_GET["token"];
$note_id=$_GET["note_id"];
$user_id=getUserFromToken($token);

$query="DELETE FROM notes WHERE owner='$user_id' AND note_id='$note_id'";

$result=$mysqli->query($query);

if(!$result){
	echo FAILURE;
}
else{
	echo SUCCESS;
}	
	
	
?>