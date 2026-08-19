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
 Bye. Hope to see you again soon!
____________________________________________________________
```

## TC3: Reject invalid commands without corrupting tasks

**Aim:** Verify required command errors and invalid mark/unmark arguments while ensuring rejected commands do not change stored tasks.

### Input

```text
todo
blah
todo read book
mark
mark two
mark 2
mark 1
unmark
unmark 1
list
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
 Bye. Hope to see you again soon!
____________________________________________________________
```
