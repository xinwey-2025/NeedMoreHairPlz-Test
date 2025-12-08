# Smart Journaling

Project folder structure
```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── journalapp/
│   │           ├── App.java                       // Application Entry Point (extends Application)
│   │           ├── controller/                    // JavaFX UI Logic (One file per FXML)
│   │           │   ├── MainController.java        // Controls the main window/sidebar layout
│   │           │   ├── AuthController.java        // Handles both Login and Registration forms
│   │           │   ├── DashboardController.java   // Controls the Landing Page/Insights
│   │           │   ├── JournalsController.java    // Controls the All Entries view
│   │           │   └── EntryEditorController.java // Controls Create/Edit forms
│   │           │
│   │           ├── model/                  //  Data Containers (The POJOs)
│   │           │   ├── User.java
│   │           │   └── Entry.java
│   │           │
│   │           └── util/                   // Core Logic & State Management
│   │               ├── Session.java        // Singleton: Holds the current logged-in User
│   │               ├── UserList.java       // Handles File I/O for users.csv (Login/Registration)
│   │               ├── UserEntries.java    // Handles File I/O for user.csv (Journal CRUD)
│   │               └── APIManager.java     // Merged: Handles Weather/Mood API calls
│   │
│   └── resources/                      // UI Files, Styles & External Data
│       ├── com/journalapp/view/        // FXML Files (Mirror Java package structure)
│       │   ├── Main.fxml               // Contains the main BorderPane and the Sidebar/NavBar
│       │   ├── Auth.fxml               // Holds the combined Login and Registration views
│       │   ├── Dashboard.fxml          // Landing Page view
│       │   ├── Journals.fxml           // All Entries view
│       │   └── EntryEditor.fxml        // Create/Edit form view
│       │
│       ├── css/                        // Stylesheets
│       │   ├── application.css         // Global styles (can create each different files)
│       │   └── theme-dark.css          // Optional theme files
│       │
data/             // Saves
├── users.csv     // User credentials file
└── entries/      // Folder containing individual journal CSVs
├── README.md
└── .gitignore

```
