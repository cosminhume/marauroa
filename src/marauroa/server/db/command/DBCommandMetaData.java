/***************************************************************************
 *                   (C) Copyright 2009-2020 - Marauroa                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package marauroa.server.db.command;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * stores meta information about a command
 *
 * @author hendrik, madmetzger
 */
class DBCommandMetaData implements Comparable<DBCommandMetaData> {

	private DBCommandPriority priority;
	private DBCommand command;
	private ResultHandle handle;
	private Thread requestingThread;
	private boolean awaitResult;
	private long processedTimestamp = -1;
	private Locale locale;


	/**
	 * creates a new DBCommandMetaData object
	 *
	 * @param command DBCommand
	 * @param handle ResultHandle
	 * @param requestingThread the thread requesting the execution of the DBCommand
	 * @param awaitResult does the thread want a result returned?
	 * @param locale locale of this operation
	 * @param priority DBCommandPriority
	 */
	public DBCommandMetaData(DBCommand command, ResultHandle handle, Thread requestingThread, boolean awaitResult, Locale locale, DBCommandPriority priority) {
		this.command = command;
		this.handle = handle;
		this.requestingThread = requestingThread;
		this.awaitResult = awaitResult;
		this.locale = locale;
		this.priority = priority;
		command.setEnqueueTime(new Timestamp(new Date().getTime()));
	}

	/**
	 * gets the command
	 *
	 * @return DBCommand
	 */
	public DBCommand getCommand() {
		return command;
	}

	/**
	 * gets the locale context of this command
	 *
	 * @return Locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * gets the requesting Thread
	 *
	 * @return requestingThread
	 */
	public Thread getRequestingThread() {
		return requestingThread;
	}

	/**
	 * is the result awaited?
	 *
	 * @return true, if the result is awaited; false if it should be thrown away
	 */
	public boolean isResultAwaited() {
		return awaitResult;
	}

	/**
	 * gets the timestamp when the command was processed.
	 *
	 * @return timestamp, -1 indicated that the command was not processed, yet.
	 */
	public long getProcessedTimestamp() {
		return processedTimestamp;
	}

	/**
	 * sets the timestamp when the command was processed.
	 *
	 * @param processedTimestamp timestamp
	 */
	public void setProcessedTimestamp(long processedTimestamp) {
		this.processedTimestamp = processedTimestamp;
	}

	/**
	 * gets the result handle.
	 *
	 * @return ResultHandle
	 */
	public ResultHandle getResultHandle() {
		return handle;
	}

	@Override
	public int compareTo(DBCommandMetaData o) {
		int p = this.priority.getPriorityValue() - o.priority.getPriorityValue();
		if (p != 0) {
			return p;
		}
		return command.getEnqueueTime().compareTo(o.command.getEnqueueTime());
	}

	@Override
    public String toString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS", Locale.ENGLISH);
	    return "[" + requestingThread.getName() + ", " + priority + ", " + format.format(command.getEnqueueTime()) + ": " + command + "]";
    }

}
