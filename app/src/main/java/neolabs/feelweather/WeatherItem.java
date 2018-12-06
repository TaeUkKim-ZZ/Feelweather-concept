package neolabs.feelweather;

import android.widget.ImageView;

public class WeatherItem {
    public String name;
    public String introduce;
    public int weatherimage;

    public WeatherItem(String name, int weatherimage, String introduce) {
        this.name = name;
        this.weatherimage = weatherimage;
        this.introduce = introduce;
    }

}
