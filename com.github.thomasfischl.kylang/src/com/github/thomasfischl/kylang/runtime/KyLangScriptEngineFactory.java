package com.github.thomasfischl.kylang.runtime;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import com.google.common.collect.Lists;

public class KyLangScriptEngineFactory implements ScriptEngineFactory {

	@Override
	public String getEngineName() {
		return "KyLangScriptEngine";
	}

	@Override
	public String getEngineVersion() {
		return "0.0.1";
	}

	@Override
	public List<String> getExtensions() {
		return Lists.newArrayList("kylang");
	}

	@Override
	public List<String> getMimeTypes() {
		return Lists.newArrayList();
	}

	@Override
	public List<String> getNames() {
		return Lists.newArrayList();
	}

	@Override
	public String getLanguageName() {
		return "Rayden Framework";
	}

	@Override
	public String getLanguageVersion() {
		return "0.0.1";
	}

	@Override
	public Object getParameter(String key) {
		throw new IllegalStateException("Not implemented");
	}

	@Override
	public String getMethodCallSyntax(String obj, String m, String... args) {
		throw new IllegalStateException("Not implemented");
	}

	@Override
	public String getOutputStatement(String toDisplay) {
		throw new IllegalStateException("Not implemented");
	}

	@Override
	public String getProgram(String... statements) {
		throw new IllegalStateException("Not implemented");
	}

	@Override
	public ScriptEngine getScriptEngine() {
		return new KyLangScriptEngine(this);
	}

}
