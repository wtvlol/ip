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
