package us.gridiron.application.payload.request;

public class InviteEmailDTO {
	private String to;
	private String subject;
	private String text;
	private String inviteCode;

	public String getTo()
	{
		return to;
	}

	public void setTo(String to)
	{
		this.to = to;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getInviteCode()
	{
		return inviteCode;
	}

	public void setInviteCode(String inviteCode)
	{
		this.inviteCode = inviteCode;
	}
}
