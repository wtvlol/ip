# Groot User Guide

// Update the title above to match the actual product name

// Product screenshot goes here

// Product intro goes here

## Adding deadlines

// Describe the action and its outcome.

// Give examples of usage

Example: `keyword (optional arguments)`

// A description of the expected outcome goes here

```
expected output
```

## Sorting deadlines (C-Sort)

Use `sort` to display deadlines from earliest to latest, including completed deadlines.
Use `sort -r` or `sort --reverse` to display the latest deadlines first instead.
Todos and events are not included; event end times are not interpreted or sorted.

### Syntax and validation

```text
sort
sort -r
sort --reverse
```

The command and flags are case-insensitive. Extra spaces, surrounding whitespace, and tabs between
the command and flag are accepted, so `SORT`, `sort    -r`, and `Sort --REVERSE` are valid too.
This flexibility applies only to `sort`; other commands keep their existing syntax.

Only one reverse flag is allowed. For example, `sort reverse`, `sort deadline`, `sort -x`,
`sort -r -r`, `sort -r --reverse`, and `sort --reverse extra` all return:

```text
 Oops! Use: sort [-r | --reverse].
```

Misspelled command names such as `sorting` and `sort-r` retain the unknown-command response:

```text
 Oops! I don't recognise that command.
```

Invalid commands do not change tasks or saved data, even when there are no deadlines.

### Ordering and task numbers

Equal dates are ordered alphabetically by task description, ignoring capitalization. Reverse changes
only date order: name ties remain A-Z. If both date and case-insensitive description are equal,
deadlines keep their relative main-list order. Numbers inside names are compared as text, not numerically.

For example, given this main list:

```text
 Here are the tasks in your list:
 1.[T][ ] groceries
 2.[D][ ] report (by: Sep 20 2026)
 3.[D][ ] beta (by: Sep 12 2026)
 4.[D][X] Alpha (by: Sep 12 2026)
 5.[E][ ] meeting (from: Mon 2pm to: 4pm)
```

`sort` returns:

```text
 Here are your deadlines sorted by date (earliest first):
 4.[D][X] Alpha (by: Sep 12 2026)
 3.[D][ ] beta (by: Sep 12 2026)
 2.[D][ ] report (by: Sep 20 2026)
```

`sort -r` and `sort --reverse` return:

```text
 Here are your deadlines sorted by date (latest first):
 2.[D][ ] report (by: Sep 20 2026)
 4.[D][X] Alpha (by: Sep 12 2026)
 3.[D][ ] beta (by: Sep 12 2026)
```

The labels remain the main-list task numbers, not positions in the sorted display. For example,
`mark 3` marks `beta`, and `unmark 4` unmarks `Alpha`. Deleting a task shifts main-list numbers,
so run `list` or `sort` again before using numbers from an older response.

If the list is empty or contains only todos and events, either direction returns:

```text
 There are no deadlines to sort.
```

A single deadline or an already-sorted list receives the normal sorted response, not an error.
These examples show response text; the console also prints its usual separator lines.

### Storage and compatibility

Sorting affects only the current response. It does not change task order, completion status, or the
saved file, and it does not create a file on first run. The next `list` shows the original order;
`find` keeps its existing behavior. New tasks still append normally, and restarting does not remember
the sorting direction. Existing saved files require no changes or migration.


## Feature XYZ

// Feature details
