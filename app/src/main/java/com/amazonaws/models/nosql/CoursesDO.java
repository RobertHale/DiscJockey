package com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "discjocky-mobilehub-894456835-Courses")

public class CoursesDO {
    private String _name;
    private List<Integer> _distances;
    private List<Integer> _pars;

    @DynamoDBHashKey(attributeName = "name")
    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return _name;
    }

    public void setName(final String _name) {
        this._name = _name;
    }
    @DynamoDBAttribute(attributeName = "distances")
    public List<Integer> getDistances() {
        return _distances;
    }

    public void setDistances(final List<Integer> _distances) {
        this._distances = _distances;
    }
    @DynamoDBAttribute(attributeName = "pars")
    public List<Integer> getPars() {
        return _pars;
    }

    public void setPars(final List<Integer> _pars) {
        this._pars = _pars;
    }

}
