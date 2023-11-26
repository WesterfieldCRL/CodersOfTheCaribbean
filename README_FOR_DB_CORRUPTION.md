# Project Merge Instructions

When merging from `main`, follow these steps carefully to ensure the `CruiseDatabase` folder is handled correctly.

## Prerequisites

Before beginning the merge process:

1. Ensure you have a `.gitattributes` file at the root of your project with the following content to handle binary files correctly:

```bash
*.dat binary
```

2. Confirm that the `CruiseDatabase` folder is fully tracked by Git. There should be no database files in `.gitignore`.

## Steps for Merging

### Exporting the Database

1. **Pull from Main**:
- Switch to your local "main" branch that is used solely for pulling from the remote `main`.

```bash
git checkout main
git pull origin main
```


2. **Generate DDL**:
- In IntelliJ's Database section, right-click on the `tables`.
- Go to `SQL Scripts` -> `Generate DDL to Clipboard`.
- Paste the clipboard content into a new SQL file with the `.sql` extension.

3. **Export Data**:
- Again, right-click on `tables`.
- Choose `Import/Export` -> `Export Data to File`.
- Select the extractor for "SQL inserts".
- Specify the output directory to an empty folder, as this will generate many files.

### Merging with Main

1. **Checkout the Target Branch**:
- Switch to the branch you intend to merge changes into.

```bash
git checkout your-target-branch
```


2. **Merge Changes**:
- Merge changes from `main` into your branch.

```bash
git merge main
```


3. **Reset the CruiseDatabase Folder**:
- After merging but before committing, reset the `CruiseDatabase` folder.

```bash
git reset HEAD CruiseDatabase/
```

This command unstages any changes that the merge introduced to the folder.

4. **Checkout CruiseDatabase Folder**:
- Checkout the `CruiseDatabase` folder from your current branch to ensure it remains unchanged.

```bash
git checkout HEAD -- CruiseDatabase/
```

### Integrating Additional Database Changes

If the merged `main` branch contains new tables or changes you want to keep:

1. **Run SQL Scripts**:
- If the database in `main` has more tables than your original database, in IntelliJ's Database section, right-click on `tables`.
- Go to `SQL Scripts` -> `Run SQL Scripts`, and run the SQL file you saved earlier to update your schema.

2. **Import New Data**:
- To import data for the new tables, right-click on `tables`.
- Choose `Import/Export` -> `Import Data from File(s)`.
- Select all the files you exported to the specified folder to import the data.

By following these steps, you can merge changes from `main` while keeping your `CruiseDatabase` folder intact and optionally integrating new database changes.
