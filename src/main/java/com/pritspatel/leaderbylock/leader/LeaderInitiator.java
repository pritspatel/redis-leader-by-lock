package com.pritspatel.leaderbylock.leader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

@Service
public class LeaderInitiator {

	private static final int LOCK_TIMEOUT = 5000;
	private static final String LEADER_LOCK = "LEADER_LOCK";
	private Jedis jedis;
	private String myApplicationId;

	@Autowired
	@Qualifier("systemMsgChannel")
	private MessageChannel messageChannel;

	public LeaderInitiator(Jedis jedis, String myApplicationId) {
		this.jedis = jedis;
		this.myApplicationId = myApplicationId;
	}

	@Scheduled(fixedDelay = 2000)
	public void tryToAcquireLock() {
		jedis.set(LEADER_LOCK, "" + myApplicationId, "NX", "PX", LOCK_TIMEOUT);
		if (this.isLeader()) {
			System.out.println("["+ myApplicationId +"] is Leader");

		} else {
			System.out.println("["+ myApplicationId +"] is on stand by. ");
		}
	}
	
	public boolean isLeader() {
		return myApplicationId.equals(jedis.get(LEADER_LOCK));
	}
	
}
