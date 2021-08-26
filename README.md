# FloatingLikes
Emit floating emojis while view is pressed

## Use
```
        likeView = LikeView(findViewById(R.id.tapView))
        likeView.setup()
```

## Demo
<img src="https://github.com/VladHumeniuk/FloatingLikes/blob/main/demo.gif" width="240"/>

## Modify
- emitted emoji
- time delay between emits
- size of emitted emoji
- duration of transition
- transition distance
- enable/disable spreading

```
        likeView = LikeView(findViewById(R.id.textView))
        likeView.setup(emoji = "\u2705",
            delay = 2000L,
            randomize = false,
            size = 20f,
            translateDuration = 1500L,
            floatHeight = 1000)
```
