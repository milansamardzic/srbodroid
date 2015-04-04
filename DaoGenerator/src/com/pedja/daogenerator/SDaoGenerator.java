/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pedja.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class SDaoGenerator
{

    public static void main(String[] args) throws Exception
    {
        Schema schema = new Schema(1, "com.srbodroid.app.model");

        addNews(schema);
        new DaoGenerator().generateAll(schema, "./app/src/main/java");
    }

    private static void addNews(Schema schema)
    {
        Entity news = schema.addEntity("News");
        news.setHasKeepSections(true);
        news.addIdProperty();
        news.addStringProperty("title");
        news.addStringProperty("link");
        news.addStringProperty("commentsLink");
        news.addStringProperty("commentsFeedLink");
        news.addStringProperty("description");
        news.addStringProperty("content");
        news.addStringProperty("author");
        news.addLongProperty("pubDate");
        news.addStringProperty("category");
        news.addLongProperty("comment_count");

        Entity image = schema.addEntity("Image");
        image.setHasKeepSections(true);
        image.addIdProperty();
        image.addStringProperty("url");

        Property newsId = image.addLongProperty("news_id").getProperty();
        ToMany newsToImage = news.addToMany(image, newsId);
        newsToImage.setName("images");

        Entity comment = schema.addEntity("Comment");
        comment.setHasKeepSections(true);
        comment.addIdProperty();
        comment.addStringProperty("link");
        comment.addStringProperty("author");
        comment.addStringProperty("pubDate");
        comment.addStringProperty("description");

        newsId = comment.addLongProperty("news_id").getProperty();
        ToMany newsToComment = news.addToMany(comment, newsId);
        newsToComment.setName("comments");


    }

}
