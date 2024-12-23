package webform.api.service.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * カウントサービス。
 * ローカルファイルを使って、サーバ上でユニークな連番の数字文字列を生成する。
 *
 */
public class CountService {
	/** Log */
	private Log log = LogFactory.getLog(getClass());

	/** カウントファイル名。 */
	private String countFileName = null;

	/**
	 * 申込カウンターファイルから現在のカウントを取得する。
	 *
	 * @param countFileName
	 * @return
	 * @throws IOException
	 */
	private int readCount(String countFileName) throws IOException {
		int count = 0;
		File countFile = new File(countFileName);
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(countFile);
			br = new BufferedReader(fr);
			String lastLine = "0";
			String buf = null;
			while ((buf = br.readLine()) != null) {
				log.info("buf=" + buf);
				lastLine = buf;
			}

			try {
				count = Integer.parseInt(lastLine);
			} catch (NumberFormatException e) {
				// ファイルの内容が数字ではない場合は初期値(0)とする。
			}

		} catch (FileNotFoundException e) {
			// ファイルが無い場合は初期値(0)とする。
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					// クローズのエラーは処理を継続する。
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// クローズのエラーは処理を継続する。
				}
			}
		}
		return count;
	}

	/**
	 * 申込カウンターファイルにカウントを書き込む。
	 *
	 * @param countFileName
	 * @param newCount
	 * @throws IOException
	 */
	private void writeCount(String countFileName, String newCount) throws IOException {
		File countFile = new File(countFileName);
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(countFile);
			bw = new BufferedWriter(fw);
			bw.write(newCount);
			bw.flush();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					// クローズのエラーは処理を継続する。
				}
			}
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					// クローズのエラーは処理を継続する。
				}
			}
		}
	}


	/**
	 * コンストラクタ。
	 */
	public CountService(String countFileName){
		this.countFileName = countFileName;
	}

	/**
	 * カウント(指定桁数0埋め数字)を生成する。
	 *
	 * @param size
	 * @return
	 * @throws IOException
	 */
	public String createCount(int size) throws IOException {
		String newCount = "";
		String format = "%0" + size + "d";

		File lockFile = new File(this.countFileName + ".lock");
		FileOutputStream lockFs = null;
		try {
			// ロックファイルを生成する。
			lockFile.deleteOnExit();
			lockFs = new FileOutputStream(lockFile);
			FileChannel lockFc = lockFs.getChannel();
			FileLock lock = lockFc.lock();

			try {
				// カウンターファイルから現在のカウントを取得する。
				int count = this.readCount(this.countFileName);
				log.info("count=" + count);

				// カウントアップしたカウントを取得する。
				newCount = String.format(format, ++count);
				log.info("newCount=" + newCount);

				if (newCount.length() > size) {
					// 桁数を超えた場合は、1から振りなおし。
					newCount = String.format(format, 1);
					log.info("桁数オーバーのため初期値の1で値を生成する。");
				}

				// カウンターファイルにカウントを書き込む。
				this.writeCount(this.countFileName, newCount);
			} finally {
				// ロックの開放
				lock.release();
			}
		} finally {
			if (lockFs != null) {
				try {
					lockFs.close();
				} catch (IOException e) {
					// クローズのエラーは処理を継続する。
				}
			}
		}

		return newCount;
	}

	/**
	 * カウント(指定桁数0埋め数字)を生成する。
	 * 引数の生成済みカウントが空（null又は空文字）でない場合は、生成済みカウントを返却する。
	 *
	 * @param generatedCount 生成済みカウント。
	 * @param size
	 * @return
	 * @throws IOException
	 */
	public String createCount(String generatedCount, int size) throws IOException {
		String newCount = "";
		String format = "%0" + size + "d";

		if (StringUtils.isEmpty(generatedCount)) {
			File lockFile = new File(this.countFileName + ".lock");
			FileOutputStream lockFs = null;
			try {
				// ロックファイルを生成する。
				lockFile.deleteOnExit();
				lockFs = new FileOutputStream(lockFile);
				FileChannel lockFc = lockFs.getChannel();
				FileLock lock = lockFc.lock();

				try {
					// カウンターファイルから現在のカウントを取得する。
					int count = this.readCount(this.countFileName);
					log.info("count=" + count);

					// カウントアップしたカウントを取得する。
					newCount = String.format(format, ++count);
					log.info("newCount=" + newCount);

					if (newCount.length() > size) {
						// 桁数を超えた場合は、1から振りなおし。
						newCount = String.format(format, 1);
						log.info("桁数オーバーのため初期値の1で値を生成する。");
					}

					// カウンターファイルにカウントを書き込む。
					this.writeCount(this.countFileName, newCount);
				} finally {
					// ロックの開放
					lock.release();
				}
			} finally {
				if (lockFs != null) {
					try {
						lockFs.close();
					} catch (IOException e) {
						// クローズのエラーは処理を継続する。
					}
				}
			}

		} else {
			newCount = generatedCount;
		}
		return newCount;
	}
}