<?php
/*
newNote - Creates a new note
Params: Note name, content, owner, session(can be null)
Returns: true/success (note is inserted with fields)
         false/failure (note not able to be inserted)
Possible Errors: NEW_NOTE_INSERTION_ERROR

           */
	include 'getUser.php';
	include 'error.php';
	
	$token = $_GET["token"];
  $user_id = getUserFromToken($token);
  $content = $_GET["content"];
	
  $note_name = $_GET["note_name"];
  $session_id = intval($_GET["session_id"]);

	if($session_id == -1)
	{
		$result=$mysqli->query("INSERT INTO notes (name, content, owner, session) VALUES ('$note_name', '$content', '$user_id', "NULL")");
	}
	else if($session_id)
	{
		$result=$mysqli->query("INSERT INTO notes (name, content, owner, session) VALUES ('$note_name', '$content', '$user_id', '$session_id')");
	}
	else
	{
		//error_match("NEW_NOTE_FAILURE");
    echo "-1";
    return false;
	}
	if($result==false)
	{
		//error_match("NEW_NOTE_FAILURE");
    echo FAILURE;
		return false;
	}
	else
	{
    $new_note_id = $mysqli->insert_id;
    	return $new_note_id;
	}
?>