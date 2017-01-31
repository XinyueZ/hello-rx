# Say hello to Rx-programming on Android


> In this repo I introduce only common methods that used when people code with RxAndroid.


## General operator

### just:

> For single element, data.

For example: ```"1", "2","3","4","5","6".....```


### fromArray:

> For array with elements, data-group.

For example: ```new String[]{"1", "2","3","4","5","6".....}```

### map:

> "Transform", convert original data to another form, like y = f(x).

For example:

```new String[]{"1", "2","3","4","5","6".....}``` ====> ```new String[]{"one", "two","three","four","five","six".....}```

### flatMap:

> "Transform", convert original data to another form, however, it wouldn't be transformed by ```1:1```  like ```1 -> one, 2 -> two```, y = f(x),
instead the ```flatMap``` is ```1:n``` which means the original data scale to more information, the y = f(f(x)).

For example:

You locate in a community. The community, ```the x``,  contains a lot houses. Your task is to input community and   output   hours-list of community, the  ```f(x)```.

The output is, ```y=f(f(x))```.

More details, see example codes.

### reduce:

> "Transform", *shrink* original data(always be collection) into another form, it would be like ```n:1``` which is reverse direction of ```flatMap```. y = f(a, b, c...) .

For example:

You have a lot houses, the list of object ``` a, b, c....`` and you want to build a community ```y```.

The output is, ```y=f(a, b, c.....)```.

## With Retrofit




