# Dinero - Personal Budgeting App

<img src="https://github.com/user-attachments/assets/1af88c1d-63b0-4aef-a787-4e952d35ce7b" width="400"/>

> Created by JESKTEK.
>
> 
> SAJANA BIDESI – ST10249843
> 
> JADIN NAICKER – ST10275486
> 
> KIAN JOSE LIMA CAMPBELL – ST10252440
> 
> EMMA MAE ATKINSON – ST10258496

---

## Table of Contents
- [Links](#links)
- [Introduction](#introduction)
- [Overview](#overview)
- [Features](#features)
- [Application Requirements](#application-requirements)
  - [Functional Requirements](#functional-requirements)
  - [Non-Functional Requirements](#non-functional-requirements)
- [Design Elements and Theme](#design-elements-and-theme)
- [Mockups and Prototypes](#mockups-and-prototypes)
- [Project Plan](#project-plan)
- [Conclusion](#conclusion)
- [Reference List](#reference-list)

---

## Links

Figma Prototype Design Link: <https://www.figma.com/design/v5kfxf10eg3DvDHpyX49Uv/PROG7313-POE---DINERO?node-id=0-1&t=N8XCfQ1HOtX1SNrq-1>  

Github Repo Source Code Link: <https://github.com/JESKTEK/DINERO.git> 

YouTube video tutorial Link: <https://youtu.be/dbIvylC07t8?si=SXxgn5JAmHXFtL_5> 

Gantt Chart Project Plan on Canva: <https://www.canva.com/design/DAGhnUT7HrQ/i7ujLOtXladlzXBWEcBf3w/edit?utm_content=DAGhnUT7HrQ&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton>

---
## Introduction

Dinero is a personal finance application designed to make budgeting warm, simple, and motivating.  
Our app combines intuitive design, helpful analytics, and gamification to create a supportive space for users to manage their finances without stress.
Dinero is not just an app — it is a *financial companion*, helping users nurture their budget like the growth of an avocado tree: slow, steady, and rewarding.

---

## Overview

- *Name:* Dinero ("Money" in Spanish, inspired by "Denarius")
- *Tech Stack:* Developed using Kotlin for the backend logic and XML for the user interface design. The app was built entirely in Android Studio, ensuring native performance and seamless integration with Android services.
- *Mascot:* Avocado — symbolizing growth, patience, and financial maturity.
- *Platform:* Android Application
- *Core Mission:* Empower users with simple, comforting tools for effective budgeting and financial tracking.

Dinero’s design focuses on *usability, security, and accessibility*, creating a positive experience that encourages users to stick to their financial goals.

---

## Features

- **User Registration and Login** (with Firebase authentication)  
- **Create Expense Categories** ("Envelopes" system)  
- **Add Expenses and Income** (with receipt image uploads)  
- **Monthly and Category Limits** (with visual progress bars)  
- **Budget Analytics Dashboard**  
  - Pie chart showing amount spent per category  
  - Bar graph comparing spending per category  
- **Gamified Goals Page**  
  - Set and track financial goals with progress meters  
  - Receive encouragement and visual achievements from Dinero the Avocado  
- **Multi-device Synchronization** (via Firebase Cloud)  
- **Tutorials and Guidance** (led by mascot Dinero)

---

## Additional Features

These added tools enhance usability, motivation, and budgeting convenience:

1. **Built-in Calculator**  
   A quick-access calculator is integrated within the app, enabling users to make calculations on the fly while budgeting or reviewing expenses.

2. **Help Chatbot**  
   A lightweight, rule-based chatbot provides system assistance, directing users to features and answering common questions — especially useful for first-time users.

3. **Wallet Page**  
   A dedicated section that allows users to input and manage their wallet balance. This acts as a quick reference to available cash and supplements the primary budget tracking system.


---

## Application Requirements

### Functional Requirements

- Secure account registration and login.
- Creation and management of budgets and expense categories.
- Expense entry with optional receipt uploads.
- Monthly budget limits and spending warnings.
- Filtering of transactions by date and category.
- View graphical financial analytics (pie charts, bar graphs).
- Gamified system awarding ranks for good budgeting.

### Non-Functional Requirements

- *Security:*  
  - User authentication with optional multi-factor authentication (MFA).
  - Regular security updates to protect financial data.
- *Scalability:*  
  - Optimized local database and Firebase cloud storage.
  - Modular code for future feature expansion.
- *Usability:*  
  - Simple UI/UX, bold typography, warm color schemes.

---

## Design Elements and Theme

Dinero’s visual identity is built around:

- **Color Palette:** Shades of green representing growth, money, and positivity.  
<img src="https://github.com/user-attachments/assets/add5696b-8c82-4fc2-9749-069d25a590a2" width="600" height="150"/>

- *Mascot:* "Dinero" the Avocado — a friendly guide for users.
<img src="https://github.com/user-attachments/assets/f5c55441-afee-4334-9475-ab2fe62b6b7a" width="200"/>

- *Typography:* Clear, bold, and easily readable fonts.
- *Icons:* Avocado-themed icons used for navigation and feedback.
- *Feedback System:* 
  - Green progress bars for staying within budget.
  - Red progress bars for overspending warnings.
- *Gamified Experience:* Progress bars, tier rankings, and visual achievements to motivate users.

> Every screen is designed to feel *supportive, friendly, and non-intimidating*.

---

## Mockups and Prototypes

*View Full Prototype:*  
[Figma Design Link](https://www.figma.com/design/v5kfxf10eg3DvDHpyX49Uv/PROG7313-POE---DINERO?node-id=0-1&t=N8XCfQ1HOtX1SNrq-1)

Key Screens Include:

- **Welcome Page (Register/Login)**  
<img src="https://github.com/user-attachments/assets/7bcff721-e795-4caa-98b5-685fee89aa6a" width="400"/>

- **Home Dashboard**  
<img src="https://github.com/user-attachments/assets/965f86f4-e610-49aa-8218-52849dddc8a1" width="400"/>

- **Budget Creation & Tracking**  
<img src="https://github.com/user-attachments/assets/e6936710-e159-48c8-b206-a1b4add15bb5" width="400"/>

- **Expense Entry with Receipt Uploads**  
<img src="https://github.com/user-attachments/assets/2002fa79-5f98-4c48-a14f-924881eb75ce" width="400"/>

- **Spending Analytics and Reports**  
<img src="https://github.com/user-attachments/assets/ee57997e-81ba-4a9d-951c-0de3efaa1fb3" width="400"/>

- **Gamified Goals Progress**  
<img src="https://github.com/user-attachments/assets/c9a61c41-757f-4fe4-9918-44a3f0b840b0" width="400"/>

---

## Project Plan

*Development Timeline:*  
[View Gantt Chart on Canva](https://www.canva.com/design/DAGhnUT7HrQ/i7ujLOtXladlzXBWEcBf3w/edit?utm_content=DAGhnUT7HrQ&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton)

### Deliverables

- *Part 1:*  
  Research and initial design prototypes.

- *Part 2:*  
  Frontend development, backend database setup, and basic user authentication. 

- *Part 3:*  
  Firebase database integration, error handling, final testing, and demonstration video.

All phases are designed to be collaborative, ensuring a well-rounded final product.

---

## Conclusion

Dinero is designed to *revolutionize personal budgeting* by creating a *supportive, enjoyable, and motivating experience*.  
Through charming design, simple functionality, and encouraging feedback, Dinero stands apart as a comforting companion in users' financial journeys.

> Manage your money. Grow your future. The Dinero way.

---

## Reference List

Android Developers, 2019. ProgressBar  |  Android Developers. [online] Available at: <https://developer.android.com/reference/android/widget/ProgressBar> [Accessed 1 May 2025].
Android Developers, 2025. AppCompatButton  |  API reference  |  Android Developers. [online] Available at: <https://developer.android.com/reference/androidx/appcompat/widget/AppCompatButton> [Accessed 18 April 2025].
GeeksforGeeks, 2019. SeekBar in Kotlin. [online] Available at: <https://www.geeksforgeeks.org/seekbar-in-kotlin>[Accessed 22 April 2025].
Montalto, P., 2017. Android: underlined text in a TextView - Paolo Montalto - Medium. [online] Available at: <https://xabaras.medium.com/android-underlined-text-in-a-textview-ff647d427bab>[Accessed 20 April 2025].
Suman, A., 2024. How to Implement DatePickerDialog in Android Using Kotlin. [online] Available at: <https://medium.com/%40abhisheksuman413/how-to-implement-datepickerdialog-in-android-using-kotlin-45c413e47464>Accessed 22 April 2025].

> *Dinero* — Grow your finances steadily, like an avocado tree.

<img src="https://github.com/user-attachments/assets/1af88c1d-63b0-4aef-a787-4e952d35ce7b" width="400"/>

> Manage your money. Grow your future. The Dinero way.

