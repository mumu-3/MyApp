/*
 *  *  Copyright (C) 2015 MummyDing
 *  *
 *  *  This file is part of Leisure( <https://github.com/MummyDing/Leisure> )
 *  *
 *  *  Leisure is free software: you can redistribute it and/or modify
 *  *  it under the terms of the GNU General Public License as published by
 *  *  the Free Software Foundation, either version 3 of the License, or
 *  *  (at your option) any later version.
 *  *                             ｀
 *  *  Leisure is distributed in the hope that it will be useful,
 *  *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *  GNU General Public License for more details.
 *  *
 *  *  You should have received a copy of the GNU General Public License
 *  *  along with Leisure.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.example.jony.myapp.reader_APP.model.daily;

/**
 * Created by mummyding on 16-1-1.
 */
public class DailyBean {

    private String date;
    private StoryBean[] stories;
    private TopStory[] top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public StoryBean[] getStories() {
        return stories;
    }

    public void setStories(StoryBean[] stories) {
        this.stories = stories;
    }

    public TopStory[] getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(TopStory[] top_stories) {
        this.top_stories = top_stories;
    }
}
