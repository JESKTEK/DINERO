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

YouTube video tutorial on emulator Link: <https://youtu.be/dbIvylC07t8?si=SXxgn5JAmHXFtL_5> 

YouTube video tutorial for mobile device: <https://youtu.be/3HuIho6aYkM> 

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
- **Multi-device Synchronization** (via Firebase Cloud)

---

## Additional Features

These added tools enhance usability and budgeting convenience:

1. **Built-in Calculator**  
   A quick-access calculator is integrated within the app, enabling users to make calculations on the fly while budgeting or reviewing expenses.

2. **Help Chatbot**  
   A lightweight, rule-based chatbot provides system assistance, directing users to features and answering common questions — especially useful for first-time users.

3. **Wallet Page**  
   A dedicated section that allows users to input and manage their wallet balance. This acts as a quick reference to available cash and supplements the primary budget tracking system.

4. **Biometric Login Support**  
   Users can log in securely using biometric methods such as fingerprint authentication, enhancing both security and convenience.

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
<p align="left">
  <img src="https://github.com/user-attachments/assets/f16232a9-327d-405f-bdae-6dad266af656" alt="Image 1" width="200" style="margin-right: 10px;">
  <img src="https://github.com/user-attachments/assets/67b56716-d34d-49a4-a9d1-43341f7f2f5c" alt="Image 2" width="200" style="margin-right: 10px;">
  <img src="https://github.com/user-attachments/assets/f7b23cf8-5e49-463d-8efb-3e5c97dbfdea" alt="Image 3" width="200">
</p>

- **Home Dashboard**  
![image](https://github.com/user-attachments/assets/6bcc18bb-fb2d-4bfa-878f-c79e7dea6ffb)

- **Budget Creation & Tracking**  
<p align="left">
  <img src="https://github.com/user-attachments/assets/8abe592a-9406-4e9e-80b9-ea7353a3b095" alt="Image 1" width="200" style="margin-right: 10px;">
  <img src="https://github.com/user-attachments/assets/1e3cc704-1fc8-4dc0-b766-e5a41b3442fe" alt="Image 2" width="200" style="margin-right: 10px;">
  <img src="https://github.com/user-attachments/assets/c0e866bb-7bb1-4e04-9d54-f3b7be0b1b3e" alt="Image 3" width="200">
</p>

- **Expense Entry with Receipt Uploads**
<p align="left">
  <img src="https://github.com/user-attachments/assets/79a81bc0-459a-4700-aa91-f0b876b3b96a" alt="Image 1" width="200" style="margin-right: 10px;">
  <img src="https://github.com/user-attachments/assets/41e96dd1-00de-4083-a7b9-d1b4314c6e6e" alt="Image 2" width="200" style="margin-right: 10px;">
</p>


- **Monthly Budget**
![image](https://github.com/user-attachments/assets/54d0d75a-a484-4f1e-acce-6fa9d40c737b)


- **Spending Analytics and Reports**  
<p align="left">
  <img src="https://github.com/user-attachments/assets/8d4481c5-2c29-4be3-9e41-599f31456afa" alt="Image 1" width="200" style="margin-right: 10px;">
  <img src="https://github.com/user-attachments/assets/6df490a0-7ff3-489f-8d77-2f328a3cc9d1" alt="Image 2" width="200" style="margin-right: 10px;">
</p>

- **Gamified Goals Progress**
<p align="left">
  <img src="https://github.com/user-attachments/assets/8804fa8e-d733-45a7-95fa-48be5c7ce973" alt="Image 1" width="200" style="margin-right: 10px;">
  <img src="https://github.com/user-attachments/assets/bab0f715-7039-46ab-b9ea-0b84d0578e07" alt="Image 2" width="200" style="margin-right: 10px;">
</p>

- **Wallet**  
![image](https://github.com/user-attachments/assets/a41b0ab5-e84a-4b6c-af09-3e811d30adde)

- **Calculator**
<p align="left">
  <img src="https://github.com/user-attachments/assets/c1dd151e-2803-4e52-a132-7086e8c33d19" alt="Image 1" width="200" style="margin-right: 10px;">
  <img src="https://github.com/user-attachments/assets/3f9b400f-b250-4077-96a5-5ea0036cdc56" alt="Image 2" width="200" style="margin-right: 10px;">
</p>

- **Chatbot**  
<p align="left">
  <img src="https://github.com/user-attachments/assets/c1dd151e-2803-4e52-a132-7086e8c33d19" alt="Image 1" width="200" style="margin-right: 10px;">
  <img src="https://github.com/user-attachments/assets/33037237-1d11-4818-917d-ea26cd5c2c4d" alt="Image 2" width="200" style="margin-right: 10px;">
</p>
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
