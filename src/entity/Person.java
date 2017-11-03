/**
 * 
 */
package entity;

/**
 * @author leal
 * @author joel
 */
public abstract class Person extends Thread implements Comparable<Person> {
	private String personName;
	private Integer timeUsingBath;

	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public Integer getTimeUsingBath() {
		return timeUsingBath;
	}
	public void setTimeUsingBath(Integer timeUsingBath) {
		this.timeUsingBath = timeUsingBath;
	}

	protected void requireBathroom() {
		Bathroom bathroom = Bathroom.getInstance();

		try {
			bathroom.getLock().lock();
			if (bathroom.addPersonInBathroom(this)) {
				this.setPriority(Thread.NORM_PRIORITY);
			} else {
				this.setPriority(Thread.MAX_PRIORITY);
				bathroom.getWaitingPersons().add(this);
			}
		} finally {
			bathroom.getLock().unlock();
		}
	}

	public void exitBathroom() {
		Bathroom bathroom = Bathroom.getInstance();
		try {
			bathroom.getLock().lock();
			bathroom.removePersonOfBathroom(this);
			bathroom.addWaitingPersonInBathroom();
		} finally {
			bathroom.getLock().unlock();
		}
	}

	public void useBathroom() {
		Bathroom bathroom = Bathroom.getInstance();
		try {
			while (!bathroom.isPersonInBathroom(this));
			sleep(timeUsingBath);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		requireBathroom();
		useBathroom();
		exitBathroom();
	}
}
