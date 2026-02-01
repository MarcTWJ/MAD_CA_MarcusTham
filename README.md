# MAD_CA_MarcusTham
This is a development done to satisfy the assignment portion for module “Mobile App development” under Ngee Ann Polytechnic for AY24/25

Basic Requirements :
- I did as shown on the screenshot requirements , but enhanced the "Mole" with an actual Mole Emoji
- This push is focused on the fundementals , getting the core requirements done.
(Commit 3)
Advanced Requirements :
- Altered UI & Added Advanced Requirements
- Rewrote a lot of the code to accomodate for such
- Altered gaming experience so now it actually feels like a mole is popping out of an empty hole , hooray

  LLM Usage :
  - I used ChatGPT 4 to clarify errors & sudden blank screening of codes
While tweaking the code to its intended function , I used chatgpt to reference code for potential fixes through the build/Logcat
Example :
Prompt : My app blank scrreens after logging in despite app being  able to run . THIS IS FROM LOGCAT 2026-02-01 21:10:21.694 (etc etc)
Response : Hey! Okay, I see exactly what’s happening here. The reason your app shows nothing past login is because of a combination of issues with how you’re passing the currentUser and rendering GameScreen/LeaderboardScreen (etc etc)

I learned how to patch such errors in navigation thats caused through not passing/calling certain things properly.


I also used it to understand how certain things worked through errors
Example : 
Prompt : FAILURE: Build failed with an exception. * Where: Build file 'C:\Users\marcu\AndroidStudioProjects\MAD_CA_MarcusTham\CoreRequirements\app\build.gradle.kts' line: 1
Response : Ah — okay, I see exactly what’s happening. This is a classic “kapt plugin conflict” in Android Studio Gradle Kotlin DSL.
Here’s what the error is telling you in plain English:
You’re trying to apply org.jetbrains.kotlin.kapt as a plugin, but it’s already on the classpath via the Kotlin Android plugin. Gradle can’t check compatibility, so it fails. 

This was a careless mistake that was fixed by refreshing gradle , but I understood how plugins worked better because of it.
