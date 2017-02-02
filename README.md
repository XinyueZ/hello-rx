# Say hello to Rx-programming on Android


> In this repo I introduce only common methods that used when people code with RxAndroid.

## Rx in Math

The idea of Rx is based on math-model ```y=f(x)``` .

No-math: what input will be transformed into other form as output.

## General operators

### just:

> For single element(s), data, translated by this operator into "*Just* now I input something..." .
For Android-users I recommend to use ```fromArray``` instead, the ```just``` would be deprecated.

For example: 

Only input something and output same, because we don't use any other operators.

```"1", "2","3","4","5","6".....```  ====> ```"1", "2","3","4","5","6".....``` 

```java

		Observable.just("one", "two", "three", "four", "five")
		          .subscribeOn(Schedulers.newThread())
		          .observeOn(AndroidSchedulers.mainThread())
		          .subscribe(new Consumer<String>() {
			                     @Override
			                     public void accept(String s) throws Exception {
				                     TextView textView = (TextView) findViewById(R.id.output_text_tv);
				                     String previous = textView.getText()
				                                               .toString();
				                     if (TextUtils.isEmpty(previous)) {
					                     previous = "Output: ";
				                     }
				                     textView.setText(new StringBuilder().append(previous)
				                                                         .append(s)
				                                                         .append(" "));
			                     }
		                     }

		          );
```

### fromArray:

> Load data from array or some other collection for elements, data-group.

For example: 

Only input something and output same, because we don't use any other operators.

```new String[]{"1", "2","3","4","5","6".....}``` ====> ```"1", "2","3","4","5","6".....``` 

```java

		Observable.fromArray(new String[] { "one",
		                                    "two",
		                                    "three",
		                                    "four",
		                                    "five" })
		          .subscribeOn(Schedulers.newThread())
		          .observeOn(AndroidSchedulers.mainThread())
		          .subscribe(new Consumer<String>() {
			                     @Override
			                     public void accept(String s) throws Exception {
				                     TextView textView = (TextView) findViewById(R.id.output_array_text_tv);
				                     String previous = textView.getText()
				                                               .toString();
				                     if (TextUtils.isEmpty(previous)) {
					                     previous = "Output array: ";
				                     }
				                     textView.setText(new StringBuilder().append(previous)
				                                                         .append(s)
				                                                         .append(" "));
			                     }
		                     }

		          );

```
### map:

> "Transform", convert original data to another form, like y = f(x).

For example:

```new String[]{"one", "two","three","four","five","six".....}``` ====> ```new String[]{"ONE", "TWO","THREE","FOUR","FIVE","SIX".....}```

```java

		Observable.fromArray(new String[] { "one",
		                                    "two",
		                                    "three",
		                                    "four",
		                                    "five" })
		          .subscribeOn(Schedulers.newThread())
		          .map(new Function<String, String>() {
			          @Override
			          public String apply(String s) throws Exception {
				          return s.toUpperCase(); // Notify for transform 1:1
			          }
		          })
		          .observeOn(AndroidSchedulers.mainThread())
		          .subscribe(new Consumer<String>() {
			                     @Override
			                     public void accept(String s) throws Exception {
				                     TextView textView = (TextView) findViewById(R.id.output_map_text_tv);
				                     String previous = textView.getText()
				                                               .toString();
				                     if (TextUtils.isEmpty(previous)) {
					                     previous = "Output map: ";
				                     }
				                     textView.setText(new StringBuilder().append(previous)
				                                                         .append(s)
				                                                         .append(" "));
			                     }
		                     }

		          );
```
### flatMap:

> "Transform", convert original data to another form, however, it wouldn't be transformed by ```1:1```  like ```1 -> one, 2 -> two```, y = f(x),
instead the ```flatMap``` is ```1:n``` which means the original data scale to more information, the y = f(g(x)).

For example:

You locate in a community. The community, ```the x```,  contains a lot houses. Your task is to input community and   output   hours-list of community, the  ```f(x)```.

The output is, ```y=f(g(x))```.

```Community``` ====> ```"House 1", "House 2","House 3","House 4","House 5","House 6".....``` 

More details, see example codes.

```java

        class House {
            private String mName;
            private String mNumber;
        
            public House(String name, String number) {
                mName = name;
                mNumber = number;
            }
        
            public House(House house1, House house2) {
                mName = house1.getName() + " + " + house2.getName();
                mNumber = house1.getNumber() + " + " + house2.getNumber();
            }
        
            public String getName() {
                return mName;
            }
        
            public String getNumber() {
                return mNumber;
            }
        
            @Override
            public String toString() {
                return "House from community [" + getName() + ", " + getNumber() + "]";
            }
        }
        
        class Community {
            private List<House> mHouseList;
        
            public Community() {
                mHouseList = null;
            }
        
            public Community(List<House> houseList) {
                mHouseList = houseList;
            }
        
        
            public List<House> getHouseList() {
                return mHouseList;
            }
        
            @Override
            public String toString() {
                return "This is a community contains " + mHouseList.size() + " houses.";
            }
        
            public void addHouse(House house) {
                if (mHouseList == null) {
                    mHouseList = new ArrayList<>();
                }
                mHouseList.add(house);
            }
        }

		List<House> houseList = Arrays.asList(new House("House 1", "Dr 1"),
		                                      new House("House 2", "Dr 2"),
		                                      new House("House 3", "Dr 3"),
		                                      new House("House 4", "Dr 4"),
		                                      new House("House 5", "Dr 5"),
		                                      new House("House 6", "Dr 6"),
		                                      new House("House 7", "Dr 7"),
		                                      new House("House 8", "Dr 8"),
		                                      new House("House 9", "Dr 9"),
		                                      new House("House 10", "Dr 10")

		);

		Community community = new Community(houseList);//With a lot houses
		Observable.fromArray(community)
		          .subscribeOn(Schedulers.newThread())
		          .flatMap(new Function<Community, Observable<House>>() {
			          @Override
			          public Observable<House> apply(Community community) throws Exception {
				          return Observable.fromIterable(community.getHouseList());//Notify for scale
			          }
		          })
		          .observeOn(AndroidSchedulers.mainThread())
		          .subscribe(new Consumer<House>() {
			                     @Override
			                     public void accept(House o) throws Exception {
				                     TextView textView = (TextView) findViewById(R.id.output_flat_map_text_tv);
				                     String previous = textView.getText()
				                                               .toString();
				                     if (TextUtils.isEmpty(previous)) {
					                     previous = "Output flat-map: \n";
				                     }
				                     textView.setText(new StringBuilder().append(previous)
				                                                         .append(o.toString())
				                                                         .append("\n"));
			                     }
		                     }

		          );
```
### reduce:

> "Transform", *shrink* original data(always be collection) into another form, it would be like ```n:1``` which is reverse direction of ```flatMap```. y = f([1:n]) .

For example:

You have a lot houses, the list of object ``` a, b, c....`` and you want to build a community ```y```.

The output is, ```y=f([1:n])```.

 ```"House 1", "House 2","House 3","House 4","House 5","House 6".....```  ====> ```Community```

```java

        class House {
            private String mName;
            private String mNumber;
        
            public House(String name, String number) {
                mName = name;
                mNumber = number;
            }
        
            public House(House house1, House house2) {
                mName = house1.getName() + " + " + house2.getName();
                mNumber = house1.getNumber() + " + " + house2.getNumber();
            }
        
            public String getName() {
                return mName;
            }
        
            public String getNumber() {
                return mNumber;
            }
        
            @Override
            public String toString() {
                return "House from community [" + getName() + ", " + getNumber() + "]";
            }
        }
        
        class Community {
            private List<House> mHouseList;
        
            public Community() {
                mHouseList = null;
            }
        
            public Community(List<House> houseList) {
                mHouseList = houseList;
            }
        
        
            public List<House> getHouseList() {
                return mHouseList;
            }
        
            @Override
            public String toString() {
                return "This is a community contains " + mHouseList.size() + " houses.";
            }
        
            public void addHouse(House house) {
                if (mHouseList == null) {
                    mHouseList = new ArrayList<>();
                }
                mHouseList.add(house);
            }
        }

		List<House> houseList = Arrays.asList(new House("House 1", "Dr 1"),
		                                      new House("House 2", "Dr 2"),
		                                      new House("House 3", "Dr 3"),
		                                      new House("House 4", "Dr 4"),
		                                      new House("House 5", "Dr 5"),
		                                      new House("House 6", "Dr 6"),
		                                      new House("House 7", "Dr 7"),
		                                      new House("House 8", "Dr 8"),
		                                      new House("House 9", "Dr 9"),
		                                      new House("House 10", "Dr 10")

		);
		
		
				Community newCommunity = new Community();//Empty.
        		Observable.fromIterable(houseList)
        		          .subscribeOn(Schedulers.newThread())
        		          .reduce(newCommunity, new BiFunction<Community, House, Community>() {
        			          @Override
        			          public Community apply(Community community, House house) throws Exception {
        				          community.addHouse(house); //Notify for shrink
        				          return community;
        			          }
        		          })
        		          .observeOn(AndroidSchedulers.mainThread())
        		          .subscribe(new Consumer<Community>() {
        			                     @Override
        			                     public void accept(Community community) throws Exception {
        				                     TextView textView = (TextView) findViewById(R.id.output_reduce_text_tv);
        				                     String previous = textView.getText()
        				                                               .toString();
        				                     if (TextUtils.isEmpty(previous)) {
        					                     previous = "Output reduce: \n";
        				                     }
        				                     textView.setText(new StringBuilder().append(previous)
        				                                                         .append(community.toString())
        				                                                         .append("\n"));
        			                     }
        		                     }
        
        		          );
```
## With Retrofit2

> This is not general manual for Retrofit2, for more information about it checkout [here](http://square.github.io/retrofit/) 
Use Retrofit to create ```Observable``` and do with operators instead using ```Call``` .


For example: Call on http://rest-service.guides.spring.io/greeting
```java


        class Hello {
            @SerializedName("id") private int mId;
            @SerializedName("content") private String mContent;
        
        
            public Hello(int id, String content) {
                mId = id;
                mContent = content;
            }
        
        
            public int getId() {
                return mId;
            }
        
            public String getContent() {
                return mContent;
            }
        }
        
        interface Service {
            @GET("greeting")
            Observable<Hello> greeting();
        
        
        }



		Retrofit retrofit = new Retrofit.Builder().baseUrl("http://rest-service.guides.spring.io/")
		                                          .addConverterFactory(GsonConverterFactory.create())
		                                          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
		                                          .build();
		Service service = retrofit.create(Service.class);
		service.greeting()
		       .subscribeOn(Schedulers.io())
		       .flatMap(new Function<Hello, Observable<String>>() {
			       @Override
			       public Observable<String> apply(Hello hello) throws Exception {
				       return Observable.fromArray("id: " + hello.getId(), ", content: " + hello.getContent());//Notify for scale
			       }
		       })
		       .observeOn(AndroidSchedulers.mainThread())
		       .subscribe(new Consumer<String>() {
			       @Override
			       public void accept(String string) throws Exception {
				       TextView textView = (TextView) findViewById(R.id.output_greeting_tv);
				       String previous = textView.getText()
				                                 .toString();
				       if (TextUtils.isEmpty(previous)) {
					       previous = "Greeting: ";
				       }
				       textView.setText(new StringBuilder().append(previous)
				                                           .append(string)
				                                           .append(""));
			       }
		       });
```




