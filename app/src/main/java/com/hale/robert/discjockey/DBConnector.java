package com.hale.robert.discjockey;

import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedList;
import com.amazonaws.models.nosql.UsersDO;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.List;

public class DBConnector {

    private DynamoDBMapper dynamoDBMapper;

    public DBConnector(){
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .build();
    }

    public void saveUser(final UsersDO user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamoDBMapper.save(user);
            }
        }).start();
    }

    public UsersDO getItem(final String name) {
        final UsersDO users[] = new UsersDO[1];
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                users[0] = dynamoDBMapper.load(UsersDO.class, name);
                Log.d("database", "run: user " + users[0].getName());
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return users[0];
    }

    public List<UsersDO> getAllUsers(){
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        return dynamoDBMapper.scan(UsersDO.class, scanExpression);
    }
}
