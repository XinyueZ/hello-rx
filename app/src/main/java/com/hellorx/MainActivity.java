package com.hellorx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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

		Community community = new Community(houseList);
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



		final Community newCommunity = new Community();
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

	}

}

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
