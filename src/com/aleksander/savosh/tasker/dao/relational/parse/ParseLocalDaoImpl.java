package com.aleksander.savosh.tasker.dao.relational.parse;

import com.aleksander.savosh.tasker.dao.relational.LocalDao;

public class ParseLocalDaoImpl<Obj> extends AbstractParseDao<Obj> implements LocalDao<Obj> {

    public ParseLocalDaoImpl(Class<Obj> objClass) {
        super(objClass, false);
    }
}