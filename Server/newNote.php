<?php
	include 'getUser.php';
	
	$token = $_GET["token"];
  $user_id = getUserFromToken($token);
	
  $note_name = $_GET["note_name"];
  $session_id = intval($_GET["session_id"]);

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