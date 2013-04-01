<?php
	
include 'getUser.php';

$content=$_GET["content"];
$token=$_GET["token"];
$note_id=$_GET["note_id"];

$user_id=getUserFromToken($token);


$result=$mysqli->query("UPDATE notes SET content='$content' WHERE note_id='$note_id' AND owner='$user_id'");

if($result=false)
{
	error_match("EDIT_NOTE_FAILURE");
	return false; 
}
else
{
	return true;
}

	
	
	
	
	
	
	
?>