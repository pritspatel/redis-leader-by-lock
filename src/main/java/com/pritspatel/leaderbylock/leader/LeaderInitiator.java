package com.pritspatel.leaderbylock.leader;

import com.pritspatel.leaderbylock.control.ControlGateway;
import org.springframework.beans.factory.annotation.Autowired;
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
	private ControlGateway controlGateway;

	public LeaderInitiator(Jedis jedis, String myApplicationId) {
		this.jedis = jedis;
		this.myApplicationId = myApplicationId;
	}

	@Scheduled(fixedDelay = 2000)
	public void tryToAcquireLock() {
		jedis.set(LEADER_LOCK, "" + myApplicationId, "NX", "PX", LOCK_TIMEOUT);
		if (this.isLeader()) {
			System.out.println("["+ myApplicationId +"] is Leader");

			Boolean isRunning = controlGateway.sendCommand("@jdbcPoller.isRunning()");
			/*System.out.println("isRunning : " + isRunning);
			if(!isRunning){
				controlGateway.sendCommand("@jdbcPoller.start()");
				System.out.println("Poller started ...");
			}*/

		} else {
			System.out.println("["+ myApplicationId +"] is on stand by. ");

			/*Boolean isRunning = controlGateway.sendCommand("@jdbcPoller.isRunning()");
			if(isRunning){
				controlGateway.sendCommand("@jdbcPoller.stop()");
				System.out.println("POLLER SHUTDOWN...");
			}*/
		}
	}
	
	public boolean isLeader() {
		return myApplicationId.equals(jedis.get(LEADER_LOCK));
	}
	
}
