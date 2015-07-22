package com.aleksander.savosh.tasker.service;

import android.util.Log;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


public class ParseCloudDaoImpl<Obj> implements CloudDao<Obj> {

    private final Class<Obj> objClass;
    private final ParseTransformer<Obj> tranformer;

    public ParseCloudDaoImpl(Class<Obj> objClass) {
        this.objClass = objClass;
        this.tranformer = new ParseTransformer<Obj>(objClass);
    }

    @Override
    public Obj create(Obj obj) {
        try {
            return createThrowExceptions(obj);
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getMessage() != null ? e.getMessage() : e.toString());
        }
        return null;
    }

    @Override
    public Obj createThrowExceptions(Obj obj) throws IllegalAccessException, ParseException, InstantiationException, DataNotFoundException, NoSuchMethodException, InvocationTargetException {
        try {
            ParseObject parseObject = new ParseObject(objClass.getSimpleName());
            for (Field field : objClass.getDeclaredFields()) {
                field.setAccessible(true);
                parseObject.put(field.getName(), field.get(obj));
            }
            parseObject.save();
            return tranformer.fromParseObject(parseObject);
        } catch (ParseException e){
            if(e.getCode() == 101){
                throw new DataNotFoundException();
            }
            throw e;
        }
    }

    @Override
    public List<Obj> read(Obj constraintObj) {
        try {
            return readThrowExceptions(constraintObj);
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getMessage() != null ? e.getMessage() : e.toString());
        }
        return new ArrayList<Obj>();
    }

    @Override
    public List<Obj> readThrowExceptions(Obj constraintObj) throws IllegalAccessException, ParseException, InstantiationException, DataNotFoundException, NoSuchMethodException, InvocationTargetException {
        Log.d(getClass().getName(), objClass.getSimpleName());
        try {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(objClass.getSimpleName());
            for(Field field : objClass.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(constraintObj);
                if(value != null) {
                    parseQuery.whereEqualTo(field.getName(), value);
                }
            }
            List<ParseObject> parseObjects = parseQuery.find();
            List<Obj> objList = new ArrayList<Obj>();
            for(ParseObject parseObject : parseObjects){
                objList.add(tranformer.fromParseObject(parseObject));
            }
            return objList;
        } catch (ParseException e){
            if(e.getCode() == 101){
                throw new DataNotFoundException();
            }
            throw e;
        }
    }

    @Override
    public Obj readFirst(Obj constraintObj) {
        List<Obj> objList = read(constraintObj);
        if(objList.size() > 0){
            return objList.get(0);
        }
        return null;
    }

    @Override
    public Obj readFirstThrowExceptions(Obj constraintObj) throws ParseException, IllegalAccessException, InstantiationException, DataNotFoundException, NoSuchMethodException, InvocationTargetException {
        List<Obj> objList = readThrowExceptions(constraintObj);
        if(objList.size() > 0){
            return objList.get(0);
        }
        throw new DataNotFoundException();
    }

    @Override
    public void delete(Obj constraintObj) {
        try {
            deleteThrowExceptions(constraintObj);
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getMessage() != null ? e.getMessage() : e.toString());
        }
    }

    @Override
    public void deleteThrowExceptions(Obj constraintObj) throws Exception {
        try {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(objClass.getSimpleName());
            for(Field field : objClass.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(constraintObj);
                if(value != null) {
                    parseQuery.whereEqualTo(field.getName(), value);
                }
            }
            List<ParseObject> parseObjects = parseQuery.find();
            Log.d(getClass().getName(),
                    " Delete " + objClass.getSimpleName() +
                    " by constraint " + constraintObj +
                    " count " + parseObjects.size());
            for(ParseObject parseObject : parseObjects){
                parseObject.delete();
            }
        } catch (ParseException e){
            if(e.getCode() == 101){
                throw new DataNotFoundException();
            }
            throw e;
        }
    }

}