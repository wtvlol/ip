# UI Test Plan

Each test case starts a fresh instance of `Groot`. Expected-output blocks contain program stdout only; console input is recorded separately.

## TC1: Exit using bye

**Aim:** Verify that Groot starts normally and exits with the farewell message when the user enters `bye`.

### Input

```text
bye
```

### Expected output

```text
____________________________________________________________
       \  |  /
     ___\_|_/___
    /   /   \   \
   /   | o o |    |
  |    |  ^  |    |
  |    \ \_/ /    |
   \    '---'    /
    \  |||||||  /
     | ||||||| |
  ___|_|||||||_|___
 /     |||||||     \
/      |||||||      \
       |||||||
      /||| |||\
     /_||| |||_\
Hello! I'm Groot.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC2: Manage todos, deadlines, and events

**Aim:** Verify that all three task types are stored polymorphically, preserve date/time text, and display the correct status after marking and unmarking.

### Input

```text
todo borrow book
deadline return book /by Sunday
deadline do homework /by no idea :-p
event project meeting /from Mon 2pm /to 4pm
mark 1
mark 2
list
unmark 2
list
delete 1
delete 1
delete 1
delete 1
bye
```

### Expected output

```text
____________________________________________________________
       \  |  /
     ___\_|_/___
    /   /   \   \
   /   | o o |    |
  |    |  ^  |    |
  |    \ \_/ /    |
   \    '---'    /
    \  |||||||  /
     | ||||||| |
  ___|_|||||||_|___
 /     |||||||     \
/      |||||||      \
       |||||||
      /||| |||\
     /_||| |||_\
Hello! I'm Groot.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] do homework (by: no idea :-p)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] borrow book
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] borrow book
 2.[D][X] return book (by: Sunday)
 3.[D][ ] do homework (by: no idea :-p)
 4.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] borrow book
 2.[D][ ] return book (by: Sunday)
 3.[D][ ] do homework (by: no idea :-p)
 4.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][X] borrow book
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [D][ ] return book (by: Sunday)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [D][ ] do homework (by: no idea :-p)
 Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC3: Reject invalid commands without corrupting tasks

**Aim:** Verify unknown commands, invalid arguments, and argumentless command boundaries while ensuring rejected commands do not change stored tasks or end the session.

### Input

```text
todo
blah
list extra
bye now
todo read book
mark
mark two
mark 2
mark 1
unmark
unmark 1
list
delete 1
bye
```

### Expected output

```text
____________________________________________________________
       \  |  /
     ___\_|_/___
    /   /   \   \
   /   | o o |    |
  |    |  ^  |    |
  |    \ \_/ /    |
   \    '---'    /
    \  |||||||  /
     | ||||||| |
  ___|_|||||||_|___
 /     |||||||     \
/      |||||||      \
       |||||||
      /||| |||\
     /_||| |||_\
Hello! I'm Groot.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Oops! A todo needs a description.
____________________________________________________________
____________________________________________________________
 Oops! I don't recognise that command.
____________________________________________________________
____________________________________________________________
 Oops! I don't recognise that command.
____________________________________________________________
____________________________________________________________
 Oops! I don't recognise that command.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
 Oops! Tell me which task to mark.
____________________________________________________________
____________________________________________________________
 Oops! The task number must be a whole number.
____________________________________________________________
____________________________________________________________
 Oops! Task 2 is not in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] read book
____________________________________________________________
____________________________________________________________
 Oops! Tell me which task to unmark.
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] read book
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] read book
 Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC4: Reject malformed deadlines and events

**Aim:** Verify specific errors for missing or empty deadline and event fields, then confirm valid dated tasks can still be added and listed.

### Input

```text
deadline return book
deadline /by Sunday
deadline return book /by
event meeting /from Mon
event /from Mon /to Tue
event meeting /from /to Tue
event meeting /from Mon /to
deadline return book /by Sunday
event meeting /from Mon /to Tue
list
delete 1
delete 1
bye
```

### Expected output

```text
____________________________________________________________
       \  |  /
     ___\_|_/___
    /   /   \   \
   /   | o o |    |
  |    |  ^  |    |
  |    \ \_/ /    |
   \    '---'    /
    \  |||||||  /
     | ||||||| |
  ___|_|||||||_|___
 /     |||||||     \
/      |||||||      \
       |||||||
      /||| |||\
     /_||| |||_\
Hello! I'm Groot.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Oops! Use: deadline DESCRIPTION /by DATE
____________________________________________________________
____________________________________________________________
 Oops! A deadline needs a description.
____________________________________________________________
____________________________________________________________
 Oops! A deadline needs a date or time after /by.
____________________________________________________________
____________________________________________________________
 Oops! Use: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
 Oops! An event needs a description.
____________________________________________________________
____________________________________________________________
 Oops! An event needs a start date or time after /from.
____________________________________________________________
____________________________________________________________
 Oops! An event needs an end date or time after /to.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] meeting (from: Mon to: Tue)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[D][ ] return book (by: Sunday)
 2.[E][ ] meeting (from: Mon to: Tue)
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [D][ ] return book (by: Sunday)
 Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [E][ ] meeting (from: Mon to: Tue)
 Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC5: Delete tasks and preserve list state

**Aim:** Verify middle, first, and last deletion with ArrayList reindexing, retained task status, invalid delete handling, and deletion from an empty list.

### Input

```text
todo alpha
deadline beta /by Fri
event gamma /from 2pm /to 3pm
mark 3
delete 2
list
delete
delete two
delete 0
delete 3
list
delete 1
delete 1
list
delete 1
bye
```

### Expected output

```text
____________________________________________________________
       \  |  /
     ___\_|_/___
    /   /   \   \
   /   | o o |    |
  |    |  ^  |    |
  |    \ \_/ /    |
   \    '---'    /
    \  |||||||  /
     | ||||||| |
  ___|_|||||||_|___
 /     |||||||     \
/      |||||||      \
       |||||||
      /||| |||\
     /_||| |||_\
Hello! I'm Groot.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] alpha
 Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] beta (by: Fri)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] gamma (from: 2pm to: 3pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [E][X] gamma (from: 2pm to: 3pm)
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [D][ ] beta (by: Fri)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] alpha
 2.[E][X] gamma (from: 2pm to: 3pm)
____________________________________________________________
____________________________________________________________
 Oops! Tell me which task to delete.
____________________________________________________________
____________________________________________________________
 Oops! The task number must be a whole number.
____________________________________________________________
____________________________________________________________
 Oops! Task 0 is not in the list.
____________________________________________________________
____________________________________________________________
 Oops! Task 3 is not in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] alpha
 2.[E][X] gamma (from: 2pm to: 3pm)
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] alpha
 Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [E][X] gamma (from: 2pm to: 3pm)
 Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Oops! Task 1 is not in the list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC6: Save every task-list change

**Aim:** Verify that add, mark, unmark, and delete commands succeed in sequence and save a deterministic task list for TC7 to load after restart.

### Input

```text
todo keep this
deadline remove this /by Friday
event team meeting /from 2pm /to 3pm
todo symbols | and \ slash
mark 1
delete 2
unmark 1
mark 1
bye
```

### Expected output

```text
____________________________________________________________
       \  |  /
     ___\_|_/___
    /   /   \   \
   /   | o o |    |
  |    |  ^  |    |
  |    \ \_/ /    |
   \    '---'    /
    \  |||||||  /
     | ||||||| |
  ___|_|||||||_|___
 /     |||||||     \
/      |||||||      \
       |||||||
      /||| |||\
     /_||| |||_\
Hello! I'm Groot.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] keep this
 Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] remove this (by: Friday)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] team meeting (from: 2pm to: 3pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] symbols | and \ slash
 Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] keep this
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [D][ ] remove this (by: Friday)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] keep this
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] keep this
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC7: Load saved tasks after restart

**Aim:** Verify that a fresh Groot process loads the task types, details, order, and completion states saved by TC6, then cleans up the shared test data.

### Input

```text
list
delete 1
delete 1
delete 1
bye
```

### Expected output

```text
____________________________________________________________
       \  |  /
     ___\_|_/___
    /   /   \   \
   /   | o o |    |
  |    |  ^  |    |
  |    \ \_/ /    |
   \    '---'    /
    \  |||||||  /
     | ||||||| |
  ___|_|||||||_|___
 /     |||||||     \
/      |||||||      \
       |||||||
      /||| |||\
     /_||| |||_\
Hello! I'm Groot.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] keep this
 2.[E][ ] team meeting (from: 2pm to: 3pm)
 3.[T][ ] symbols | and \ slash
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][X] keep this
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [E][ ] team meeting (from: 2pm to: 3pm)
 Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] symbols | and \ slash
 Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```
