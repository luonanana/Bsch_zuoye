package com.bestsch.zuoye.service.impl;

import com.bestsch.utils.DException;
import com.bestsch.zuoye.dao.UserAnswerRepository;
import com.bestsch.zuoye.mem.UserAnswerMem2;
import com.bestsch.zuoye.model.UserAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class StatService implements Runnable {

	@Autowired
	private UserAnswerRepository userAnswerRepository;

	@Autowired
	private UserAnswerMem2 userAnswerMem2;

	//统计数据
	private Map<String, Map<String, Object>> data = new HashMap<>();

	// private Integer count = 0;
	// 线程池
	private final LinkedBlockingQueue<Runnable> queue;
	private ExecutorService executor;

	public StatService() {
		queue = new LinkedBlockingQueue<>();
		executor = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, queue);

		(new Thread(this)).start();
	}

	private void work() {
		/*System.out.println("启动统计开始。。。。");

		Worker userRecordWorker = new Worker("", "user_answer_sta");
		executor.execute(userRecordWorker);
*/
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}

		while (true) {
			try {
				work();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				Thread.sleep(300000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void setData(String key, Map<String, Object> items) {
		if (items != null)
			data.put(key, items);
	}

	public Map<String, Object> getStatData(String key) {
		return data.get(key);
	}

	public Object getAll() {
		return data;
	}

	class Worker implements Runnable {
		private String key;
		private String type;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Worker(String key, String type) {
			this.key = key;
			this.type = type;
		}

		@Override
		public void run() {
			try {
				Map<String, Object> items = doStat();
				setData(key, items);
				// System.out.println(data.get(key));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		protected Map<String, Object> doStat() throws DException, Exception {
			Map<String, Object> items = new HashMap<>();
			if(type.equals("user_answer_sta")){

				List<UserAnswer> records = userAnswerRepository.findAll();
				userAnswerMem2.saveExecutionList(records);
			}

			return items;
		}
	}
}
