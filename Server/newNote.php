<?php
	include 'database.php';
	include 'error.php';
	include 'getUser.php';
	
	$session_id = intval($_GET["session_id"]);
	
	$token = $_GET["token"];
	$note_name = $_GET["note_name"];
	$user_id = validate($token);
	if($user_id == -1)
	{
		error_match("USER_NOT_FOUND");
		exit;
	}
	$user_id=getUserFromToken($token);
	if($session_id == -1)
	{
		$result=$mysqli->query("INSERT INTO notes (session_id, name, passcode, owner) VALUES ("NULL", '$note_name', '$token', '$user_id')");
	}
	else if($session_id)
	{
		$result=$mysqli->query("INSERT INTO notes (session_id, name, passcode, owner) VALUES ('$session_id', '$note_name', '$token', '$user_id')");
	}
	else
	{
		error_match("UNKNOWN_ERROR");
	}
	if($result==false)
	{
		error_match("NEW_NOTE_FAILURE");
	}
?>