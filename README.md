# OInvestigation
My investigation of Android O. It's not exhaustive. 


- Alrams ignored after 1 minute. Service killed (if App is in BACKGROUND)
- Alarms DO wake up Broadcast recivers and from there it's possible to start service!!!
  - BUT only with Alarm! ON target 25
  - If broadcast is send from backgroun -> IllegalStateException (we don't do this, so it's ok)
  
- Not able to start service from background. Alarms are ignored!
- JobScheduler seems to work even in background
- JobServices are killed after 1 minute also; which means app priority goes down --- re-check, I think this is wrong.
- ON_BOOT_COMPLETED can start service ONLY if target sdk 25
